# Content is parsed with org.json (no reflection). Nothing extra to keep.

# WorkManager (pulled in by play-services-ads) creates its Room database by reflection. R8 full
# mode removed the generated constructor and the release app crashed on launch:
# "Failed to create an instance of androidx.work.impl.WorkDatabase".
-keep class * extends androidx.room.RoomDatabase { <init>(); }
-keep class androidx.work.impl.WorkDatabase_Impl { *; }
