# Keep rules for ML Kit and CameraX (placeholder, adjust as needed when adding code)
# Retain classes used by ML Kit reflection if needed.
# -keep class com.google.mlkit.** { *; }
# -keep class com.google.android.gms.internal.mlkit_** { *; }

# Keep ViewBinding/Navigation safe args generated classes
-keep class **Binding { *; }
-keep class * implements androidx.navigation.NavDirections { *; }

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Do not obfuscate model classes to aid debugging initially
# -keep class com.moparvindecoder.domain.model.** { *; }
