package com.example.android.meallogger.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {MealData.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SavedMealsDAO savedMealsDao();
    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "meals_db")
                                    .addMigrations(MIGRATION_1_2)
                                    .build();
                }
            }
        }

        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE meals"
                    + " ADD COLUMN totalTransFat TEXT");
        }
    };
}