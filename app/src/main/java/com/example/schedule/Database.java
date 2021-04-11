package com.example.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Реализация работы с базой данных
 * @author Abrashov
 */
public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "database", null, 1);
    }

    /**
     * Обработчик события создания новой базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS TEACHER (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS DISCIPLINE (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS AUDITORY (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS GROUPS (Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS SCHEDULE (Id INTEGER PRIMARY KEY AUTOINCREMENT, ScheduleDate LONG, PairNumber INTEGER, TeacherId INTEGER, DisciplineId INTEGER, AuditoryId INTEGER, GroupId INTEGER)");
        generateSampleData(db);
    }

    /**
     * Генерация данных при первоначальном заполнении БД
     * @param db
     */
    void generateSampleData(SQLiteDatabase db) {
        if (getSimpleItems(db, "TEACHER").size() > 0) {
            return;
        }
        ArrayList<SimpleItem> groups = SampleScheduleGenerator.generateGroups();
        ArrayList<SimpleItem> teachers = SampleScheduleGenerator.generateTeachers();
        ArrayList<SimpleItem> disciplines = SampleScheduleGenerator.generateDisciplines();
        ArrayList<SimpleItem> auditories = SampleScheduleGenerator.generateAuditories();

        for (SimpleItem group : groups) {
            createSimpleItem(db,"GROUPS", group.name);
        }
        for (SimpleItem teacher : teachers) {
            createSimpleItem(db, "TEACHER", teacher.name);
        }
        for (SimpleItem discipline : disciplines) {
            createSimpleItem(db, "DISCIPLINE", discipline.name);
        }
        for (SimpleItem auditory : auditories) {
            createSimpleItem(db, "AUDITORY", auditory.name);
        }

        ArrayList<ScheduleItem> scheduleItems = SampleScheduleGenerator.genetateSampleSchedule(
                getSimpleItems(db,"GROUPS"),
                getSimpleItems(db, "DISCIPLINE"),
                getSimpleItems(db, "TEACHER"),
                getSimpleItems(db,"AUDITORY"),
                new Date(121, 02, 01), //01.03.2021
                new Date(121, 04, 31)); //31.05.2021
        for (ScheduleItem item : scheduleItems) {
            createScheduleItem(db, item.date, item.pairNumber, item.auditoryId, item.groupId, item.disciplineId, item.teacherId);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createSimpleItem(String tableName, String itemName){
        SQLiteDatabase db = this.getReadableDatabase();
        createSimpleItem(db, tableName, itemName);
    }

    void createSimpleItem(SQLiteDatabase db, String tableName, String itemName) {
        ContentValues values = new ContentValues();
        values.put("Name", itemName);
        db.insert(tableName, null, values);
    }

    public void updateSimpleItem(String tableName, int itemId, String itemName){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", itemName);
        db.update(tableName, values, "Id=?", new String[]{String.valueOf(itemId)});
    }

    public void deleteSimpleItem(String tableName, int itemId){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(tableName, "Id=?", new String[]{String.valueOf(itemId)});
    }

    public ArrayList<SimpleItem> getSimpleItems(String tableName){
        SQLiteDatabase db = this.getReadableDatabase();
        return getSimpleItems(db, tableName);
    }

    ArrayList<SimpleItem> getSimpleItems(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select Id, Name from " + tableName + " order by NAME", new String[0]);
        ArrayList<SimpleItem> result = new ArrayList<SimpleItem>();
        while (cursor.moveToNext()) {
            SimpleItem item = new SimpleItem(cursor.getInt(0), cursor.getString(1));
            result.add(item);
        }
        cursor.close();
        return result;
    }

    public void createScheduleItem(Date date, int pairNumber, int auditoryId, int groupId, int disciplineId, int teacherId){
        SQLiteDatabase db = this.getReadableDatabase();
        createScheduleItem(db, date, pairNumber, auditoryId, groupId, disciplineId, teacherId);
    }

    void createScheduleItem(SQLiteDatabase db, Date date, int pairNumber, int auditoryId, int groupId, int disciplineId, int teacherId) {
        ContentValues values = new ContentValues();
        values.put("ScheduleDate", date.getTime());
        values.put("PairNumber", pairNumber);
        values.put("AuditoryId", auditoryId);
        values.put("GroupId", groupId);
        values.put("DisciplineId", disciplineId);
        values.put("TeacherId", teacherId);
        db.insert("SCHEDULE", null, values);
    }

    public void updateScheduleItem(int id, Date date, int pairNumber, int auditoryId, int groupId, int disciplineId, int teacherId) {
        ContentValues values = new ContentValues();
        values.put("ScheduleDate", date.getTime());
        values.put("PairNumber", pairNumber);
        values.put("AuditoryId", auditoryId);
        values.put("GroupId", groupId);
        values.put("DisciplineId", disciplineId);
        values.put("TeacherId", teacherId);
        SQLiteDatabase db = this.getReadableDatabase();
        db.update("SCHEDULE", values, "Id=?", new String[]{String.valueOf(id)});
    }

    public void deleteScheduleItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("SCHEDULE", "Id=?", new String[]{String.valueOf(id)});
    }

    /**
     * Получение из БД расписания занятий с указанным фильтром
     * @param date фильтр по дате
     * @param auditoryId фильтр по аудитории
     * @param groupId фильтр по группе
     * @param disciplineId фильтр по дисциплине
     * @param teacherId фильтр по преподавателю
     * @return Возвращает коллекцию элементов расписания, упорядоченную по времени
     */
    public ArrayList<ScheduleItem> getScheduleItems(Date date, int auditoryId, int groupId, int disciplineId, int teacherId) {
        String sql = "select s.Id, s.PairNumber, s.AuditoryId, s.GroupId, s.DisciplineId, s.TeacherId, "
                + "a.Name AuditoryName, g.Name GroupName, d.Name DisciplineName, t.Name TeacherName, s.ScheduleDate "
                + "from SCHEDULE s "
                + "join AUDITORY a on s.AuditoryId = a.Id "
                + "join GROUPS g on s.GroupId = g.Id "
                + "join DISCIPLINE d on s.DisciplineId = d.Id "
                + "join TEACHER t on s.TeacherId = t.Id "
                + "where ScheduleDate = " + String.valueOf(date.getTime());
        if (auditoryId > 0) {
            sql += " and s.AuditoryId=" + String.valueOf(auditoryId);
        }
        if (groupId > 0) {
            sql += " and s.GroupId=" + String.valueOf(groupId);
        }
        if (disciplineId > 0) {
            sql += " and s.DisciplineId=" + String.valueOf(disciplineId);
        }
        if (teacherId > 0) {
            sql += " and s.TeacherId=" + String.valueOf(teacherId);
        }
        sql += " order by s.PairNumber";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[0]);
        ArrayList<ScheduleItem> result = new ArrayList<ScheduleItem>();
        while (cursor.moveToNext()) {
            ScheduleItem item = new ScheduleItem();
            item.id = cursor.getInt(0);
            item.pairNumber = cursor.getInt(1);
            item.auditoryId = cursor.getInt(2);
            item.auditory = cursor.getString(6);
            item.groupId =cursor.getInt(3);
            item.group = cursor.getString(7);
            item.disciplineId =cursor.getInt(4);
            item.discipline = cursor.getString(8);
            item.teacherId =cursor.getInt(5);
            item.teacher = cursor.getString(9);
            item.date = new Date(cursor.getLong(10));
            result.add(item);
        }
        cursor.close();
        return result;
    }
}