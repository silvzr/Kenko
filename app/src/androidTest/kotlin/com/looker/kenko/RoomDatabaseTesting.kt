package com.looker.kenko

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.looker.kenko.data.local.KenkoDatabase
import com.looker.kenko.data.local.MIGRATION_1_2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTesting {

    private val DB_NAME = "test.db"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        KenkoDatabase::class.java,
    )

    @Test
    fun migrate1To2() {
        val db = helper.createDatabase(DB_NAME, 1)
        db.use {
            it.execSQL("""INSERT INTO Exercise (name, target, isIsometric) VALUES ('Curls', 'Biceps', 0)""")
        }
        helper.runMigrationsAndValidate(DB_NAME, 2, true, MIGRATION_1_2)
    }

}