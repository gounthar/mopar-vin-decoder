# General Coding Habits

**Project**: Mopar VIN Decoder

---

## 1. Efficiency & Performance

- Enable **StrictMode** in dev builds.
- Use `RecyclerView.setHasFixedSize(true)` and DiffUtil.
- Use **Coil** or **Glide** for camera previews.
- Use correct coroutines dispatcher:
    - `Dispatchers.IO` → I/O work
    - `Dispatchers.Default` → CPU tasks

---

## 2. Maintainability & Scalability

- Follow **SOLID principles**.
- Use **Hilt** for dependency injection.
- Avoid “God classes” (like giant Utils).
- Extract constants into `Constants.kt`.

---

## 3. Error Handling & Robustness

- Fail gracefully with clear messages.
- Use **Result sealed classes** instead of throwing exceptions.
- Example:
  ```kotlin
  sealed interface Result<out T> {
      data class Success<T>(val data: T) : Result<T>
      data class Error(val exception: Exception) : Result<Nothing>
  }
Use Timber for logging.

## 4. Resource Management
 - Respect lifecycle (remove observers, unregister listeners).
 - Always close Cursor, Scanner, or Closeable objects.
 - Avoid holding onto Context in long-lived objects → use ApplicationContext.
