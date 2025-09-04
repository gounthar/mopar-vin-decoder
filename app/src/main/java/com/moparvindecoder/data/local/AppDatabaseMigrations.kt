package com.moparvindecoder.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Central place to register all Room migrations.
 *
 * Keep [ALL] updated when adding new migrations, e.g.:
 *
 * val MIGRATION_1_2 = object : Migration(1, 2) {
 *     override fun migrate(db: SupportSQLiteDatabase) {
 *         // Example: add a new nullable column to avoid data loss
 *         db.execSQL("ALTER TABLE vin_history ADD COLUMN notes TEXT")
 *     }
 * }
 *
 * Then include it in [ALL]:
 * val ALL: Array<Migration> = arrayOf(MIGRATION_1_2)
 */
object AppDatabaseMigrations {

    // Register all migrations here as you bump the DB version (in AppDatabase.version)
    // For version 1 (initial release), there are no migrations yet.
    val ALL: Array<Migration> = emptyArray()

    // Template for future migrations (copy and adapt):
    // val MIGRATION_X_Y = object : Migration(X, Y) {
    //     override fun migrate(db: SupportSQLiteDatabase) {
    //         // SQL statements to transform schema without losing data
    //     }
    // }
}
