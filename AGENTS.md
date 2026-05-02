# AGENTS.md

## Repository context

- Repository: `Aleksey095599/alligator-market`
- Backend: Spring Boot 4.
- Frontend: Angular / TypeScript.
- The project contains Docker Compose configuration.
- Development is primarily done in IntelliJ IDEA Ultimate.

## Core engineering values

- Prefer clear, DDD-oriented design.
- Domain models and business rules have priority over framework convenience.
- Keep the folder structure explicit and easy to understand: everything should be in its proper place.
- Prefer thoughtful simplicity: simple code is valuable only when it preserves responsibility boundaries.
- Avoid overengineering but leave natural extension points where the domain is likely to grow.
- Preserve existing project conventions unless they clearly conflict with these instructions.

## General working policy

- Before changing architecture, inspect the existing package structure and current conventions.
- For small fixes, make the smallest correct change.
- For feature work, first identify the domain meaning, aggregate root, layer boundaries, and expected lifecycle.
- Prefer minimal, reviewable diffs.
- Do not add new production dependencies unless they are clearly justified.
- Do not commit, push, merge, deploy, or create pull requests unless explicitly requested.
- When uncertain between two designs, choose the simpler design that keeps architectural boundaries clean.

## Verification policy

- For non-trivial changes, run the smallest relevant verification command when practical.
- Prefer focused tests/checks over full project-wide commands.
- It is acceptable to run normal local tests, linting, type checks, or builds when they are relevant to the change.
- Ask before running commands that are destructive, very long-running, require Docker, start external services, perform database migrations, deploy, push, or modify remote resources.
- Always report which verification commands were run and their result.
- If verification was not run, explain why and name the exact command that should be run next.

## Code style and comments

- Write code according to strong industry practices, without noise.
- Keep names precise and role-oriented.
- Avoid comments that merely repeat the code.
- Add Russian comments only where they explain non-obvious business meaning, architectural intent, or an important logical chain.
- Short Russian inline comments are acceptable when they make complex logic easier to follow.
- Symbols such as arrows may be used in comments only when they genuinely improve clarity.
- Logs, exception messages, validation messages, API messages, and external-facing technical text must be written in English.
- Code identifiers must be written in English.

## Commit message generation

- Use concise, conventional commit messages.
- Prefer the format: `type(scope): short summary`.
- Keep the message specific, but without noise.
- Examples:
    - `fix(market): resolve constraint error mapping`
    - `feat(product): add whole-aggregate replacement use case`
    - `refactor(config): make product wiring explicit`

## Backend architecture: Spring / DDD

### Spring wiring convention

- `config/**` contains only Spring wiring:
    - `@Configuration`
    - `@Bean`
    - `@Import`
    - `@EnableConfigurationProperties`
    - context-level infrastructure beans
- Every `*Config` must be self-contained.
- If beans from one config require beans from another config, dependencies must be connected explicitly through `@Import`.
- External settings classes must be named `*Properties`.
- `*Properties` classes must live outside `config/**`.
- Connect external settings explicitly through `@EnableConfigurationProperties(...)`.
- Do not create domain models, application services, repository adapters, mappers, or meaningful feature beans through stereotype annotations such as:
    - `@Component`
    - `@Service`
    - `@Repository`
- Meaningful feature beans must be assembled explicitly through `config/**`.
- Do not duplicate bean names as raw strings.
- Use `BEAN_...` constants in `@Bean` and `@Qualifier`.

### Canonical feature architecture

- Design each feature as a self-contained vertical slice around one domain meaning and one main aggregate root.
- A feature should be split into five independent roles:
    - `domain`
    - `application`
    - `api`
    - `infrastructure/persistence`
    - `config/wiring`

### Domain layer

- `domain` contains only the subject model:
    - aggregate root
    - value objects
    - internal aggregate elements
    - domain services
    - repository ports
- Domain must not depend on:
    - Spring
    - HTTP
    - SQL
    - JPA
    - jOOQ
    - UI
    - framework annotations
- Domain objects should express business meaning, not database structure or API DTO shape.
- Domain invariants should live inside the domain model, not in controllers or persistence adapters.

### Application layer

- `application` contains use case orchestration:
    - command scenarios
    - query scenarios
    - application ports
    - application exceptions
- Application coordinates domain objects and external dependencies.
- Application must not contain:
    - SQL
    - HTTP details
    - framework magic
    - controller mapping
    - persistence mapping
- Application services should describe business use cases, not technical operations.

### API layer

- `api` contains transport adapters:
    - controllers
    - request DTOs
    - response DTOs
    - API mappers
    - feature-specific exception handling
- API must not contain business logic.
- API must not contain persistence logic.
- API DTOs must not dictate the shape of the domain model.

### Infrastructure / persistence layer

- `infrastructure/persistence` contains implementations of ports:
    - jOOQ adapters
    - JPA adapters
    - SQL adapters
    - external system integrations
    - query adapters
- Persistence adapters map between database representations and domain/application models.
- Do not leak database records, generated jOOQ classes, JPA entities, or SQL details into the domain.
- Infrastructure must not make domain or application architecture decisions.
- Query/read-side models may be separate from write-side domain models.

### Config / wiring layer

- `config/wiring` contains explicit Spring bean assembly.
- Feature beans should be wired with:
    - `@Configuration`
    - `@Bean`
    - `@Import`
    - `@Qualifier`
    - `BEAN_...` constants
- Do not rely on hidden component scanning for application, persistence, or business beans.

## Aggregate and repository rules

- Each aggregate root should usually have one main repository port.
- Do not create separate domain repositories for internal aggregate elements unless they are independent aggregate roots.
- Start feature design with whole-aggregate lifecycle operations:
    - create the aggregate as a whole
    - replace the aggregate as a whole
    - delete the aggregate as a whole
    - read the aggregate as a whole
- Add narrower operations only after the base lifecycle is stable.
- Query/read-side can be designed separately from write-side.
- UI tables, dashboard views, and DTOs must not dictate the write-side domain model.

## Backend boundary self-check

Before finishing the backend feature work, check:

- Can the feature structure be understood from folders without reading implementation?
- Does the aggregate root have one clear entry point through a repository port?
- Are SQL, HTTP, Spring, JPA, jOOQ, and UI details absent from the domain?
- Are use case orchestration and persistence separated?
- Is the domain not shaped by UI tables or DTOs?
- Can new scenarios be added without rebuilding the feature foundation?
- Does each class have one architectural role and one reason to change?
- If a class mixes use case orchestration, SQL, and HTTP mapping, the boundary is wrong.

## Frontend architecture: Angular / TypeScript

### Frontend responsibility

- Treat frontend as a serious product layer, not as a collection of page scripts.
- Codex should make reasonable Angular architecture decisions without requiring the user to choose framework-specific details.
- Ask the user only when the decision changes product behavior, UX meaning, API contracts, or adds significant dependencies.
- Frontend must respect backend domain meaning but must not force backend domain design to match UI tables or screens.
- Prefer clear, maintainable UI architecture over clever abstractions.

### Angular style principles

- Follow existing project conventions first.
- If the project does not already establish a convention, prefer modern Angular practices.
- Keep Angular code consistent within each file and feature.
- Use TypeScript strictly and avoid `any` unless there is a strong reason.
- Prefer explicit, readable types over implicit complex inference when it improves maintainability.
- Keep file names and class names aligned with their responsibility.
- Prefer one meaningful thing per file.

### Frontend feature structure

Prefer a feature-oriented structure. A typical feature may contain:

- `pages/` — route-level containers/screens.
- `components/` — reusable or feature-specific presentational components.
- `data-access/` — HTTP clients, API DTOs, API mappers.
- `state/` — feature state, facades, stores, signal-based state.
- `models/` — frontend models and view models.
- `forms/` — form builders, validators, form-specific helpers.
- `routes/` — feature routing when useful.

Rules:

- Keep feature code close to the feature.
- Shared code belongs in shared/common areas only when it is genuinely reused.
- Do not move code to shared prematurely.
- Avoid creating a custom frontend framework inside the application.
- Do not introduce global state management unless the feature genuinely needs it.

### Components

- Keep components small and focused.
- A component should have one UI responsibility.
- Prefer presentational components for reusable UI pieces.
- Route/page components may orchestrate feature state and user flows.
- Avoid giant components that mix layout, state, API calls, validation, and mapping.
- Avoid heavy calculations in templates.
- Prefer clear inputs and outputs.
- Do not place backend/business rules directly in templates.
- Use semantic HTML before adding extra ARIA.
- Keep component styles scoped where practical.

### State management

- Keep the local UI state local.
- Keep the feature state inside the feature.
- Use signals for local synchronous state and derived state when appropriate.
- Use RxJS for asynchronous streams, event flows, and HTTP composition when appropriate.
- Expose readonly state to components when possible.
- Avoid mutable shared state without a clear owner.
- Avoid global stores for simple feature-local state.
- Add a facade/store only when it reduces complexity.

### HTTP and API integration

- HTTP calls belong in `data-access` services or equivalent feature-specific API adapters.
- Components should not manually assemble low-level HTTP details.
- Use typed request and response DTOs.
- Map API DTOs into frontend models/view models when this improves clarity.
- Do not leak unstable API response shapes across the UI.
- Handle loading, empty, error, and success state explicitly.
- Technical error messages should be in English.
- Do not expose secrets, tokens, or sensitive implementation details in frontend code.

### Forms

- Prefer typed reactive forms for non-trivial forms.
- Template-driven forms are acceptable only for simple cases or existing project convention.
- Keep validation rules explicit.
- Keep validation messages clear and consistent.
- Separate form construction/mapping from visual markup when the form becomes complex.
- Do not let form DTOs dictate backend domain models.

### Routing

- Prefer lazy-loaded feature routes for larger features.
- Keep route guards and resolvers focused on navigation and loading concerns.
- Do not put business rules into route guards unless the rule is truly navigation-specific.
- Keep route names and paths meaningful.

### Styling and UX

- Prefer clean, boring, maintainable CSS over clever visual hacks.
- Keep the layout responsive.
- Use component-scoped styles unless a global style is truly global.
- Avoid unnecessary CSS complexity.
- Preserve visual consistency with the existing application.
- Do not add UI libraries or icon packs unless clearly justified.

### Accessibility

- Use semantic HTML.
- Ensure interactive elements are keyboard-accessible.
- Use labels for form controls.
- Use `alt` text for meaningful images.
- Use ARIA only when semantic HTML is not enough.
- Do not remove accessibility attributes without reason.
- Loading, error, and empty states should be understandable to users.

### Frontend performance

- Prefer simple performance-conscious design from the start.
- Avoid unnecessary re-rendering and repeated expensive computations.
- Use stable tracking for rendered lists.
- Prefer derived state over recalculating values in templates.
- Do not optimize prematurely but avoid obviously wasteful patterns.
- Keep bundles smaller by avoiding unnecessary dependencies.

### Frontend testing and verification

- Add or update tests for important frontend behavior when the change is non-trivial.
- Test behavior, not Angular internals.
- Prefer focused component/service tests over broad brittle tests.
- Run focused frontend checks when relevant and practical.
- Report the exact command and result.

## Cross-boundary rules

- Backend domain must not be shaped by frontend DTOs, tables, or dashboards.
- Frontend view models may be shaped for UX convenience.
- Use mappers between backend API DTOs and frontend models when direct usage would blur meaning.
- Keep business invariants on the backend/domain side.
- Keep frontend validation useful for UX, but do not treat it as the only enforcement of business rules.
- API contracts should be explicit and stable.

## Final response format

When finishing a task, respond with:

1. Summary
    - What changed and why.
2. Changed files
    - List important files and their roles.
3. Verification
    - Commands run and result, or why verification was not run.
4. Notes
    - Risks, tradeoffs, or follow-up suggestions only when relevant.

Keep final responses concise and focused.
