# Git Commit Conventions

**Project**: Mopar VIN Decoder  

We use the **Conventional Commits** specification.

---

## 1. Structure

<type>(<scope>): <short description>

[optional body]

[optional footer(s)]

---

## 2. Types

- **feat**: A new feature  
- **fix**: A bug fix  
- **docs**: Documentation only  
- **style**: Formatting (no logic changes)  
- **refactor**: Code restructure without new features/fixes  
- **test**: Tests only  
- **chore**: Maintenance (deps, build, CI)  

---

## 3. Scope Examples

- `vin-decoder`  
- `scanner`  
- `history`  
- `database`  
- `ui`  
- `build`  

---

## 4. Examples

feat(vin-decoder): implement 1966-1974 VIN parsing logic

fix(scanner): resolve crash on camera permission denied

docs(readme): add build instructions for contributors

chore(build): update Kotlin version to 1.7.0

---

## 5. Best Practices

- Keep subject lines **≤50 chars**, imperative mood (“Add” not “Added”).  
- Wrap body text at **72 chars**.  
- Use footers for issues: `Closes #15` or `Ref: #22`.  