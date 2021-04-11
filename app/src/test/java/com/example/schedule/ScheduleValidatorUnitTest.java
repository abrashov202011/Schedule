package com.example.schedule;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Модульные тесты для класса ScheduleValidator
 */
public class ScheduleValidatorUnitTest {

    @Test
    public void addItem_intersectsByAuditory_isIncorrect() {
        List<ScheduleItem> schedule = createSampleSchedule();
        ScheduleItem item = new ScheduleItem();
        item.id = 0;
        item.date = new Date(2021, 04, 01);
        item.auditoryId = 10;
        item.teacherId = 0;
        item.groupId = 0;
        item.pairNumber = 1;
        String msg = ScheduleValidator.ValidateItem(schedule, item);
        assertEquals("Аудитория занята в указанную дату и пару.", msg);
    }

    @Test
    public void addItem_intersectsByGroup_isIncorrect() {
        List<ScheduleItem> schedule = createSampleSchedule();
        ScheduleItem item = new ScheduleItem();
        item.id = 0;
        item.date = new Date(2021, 04, 01);
        item.auditoryId = 0;
        item.teacherId = 0;
        item.groupId = 1000;
        item.pairNumber = 1;
        String msg = ScheduleValidator.ValidateItem(schedule, item);
        assertEquals("Группа в указанную дату и пару находится на другой лекции.", msg);
    }

    @Test
    public void addItem_intersectsByTeacher_isIncorrect() {
        List<ScheduleItem> schedule = createSampleSchedule();
        ScheduleItem item = new ScheduleItem();
        item.id = 0;
        item.date = new Date(2021, 04, 01);
        item.auditoryId = 0;
        item.teacherId = 1;
        item.groupId = 0;
        item.pairNumber = 1;
        String msg = ScheduleValidator.ValidateItem(schedule, item);
        assertEquals("Преподаватель в указанную дату и пару находится на другой лекции.", msg);
    }

    @Test
    public void addItem_notIntersects_isCorrect() {
        List<ScheduleItem> schedule = createSampleSchedule();
        ScheduleItem item = new ScheduleItem();
        item.id = 0;
        item.date = new Date(2021, 04, 01);
        item.auditoryId = 10;
        item.teacherId = 10;
        item.groupId = 1000;
        item.pairNumber = 8;
        String msg = ScheduleValidator.ValidateItem(schedule, item);
        assertEquals(null, msg);
    }

    @Test
    public void editItem_notIntersects_isCorrect() {
        List<ScheduleItem> schedule = createSampleSchedule();
        ScheduleItem item = new ScheduleItem();
        item.id = 1;
        item.date = new Date(2021, 04, 01);
        item.teacherId = 1;
        item.auditoryId = 10;
        item.groupId = 1000;
        item.pairNumber = 1;
        String msg = ScheduleValidator.ValidateItem(schedule, item);
        assertEquals(null, msg);
    }

    ArrayList<ScheduleItem> createSampleSchedule() {
        ArrayList<ScheduleItem> schedule = new ArrayList<ScheduleItem>();

        ScheduleItem item1 = new ScheduleItem();
        item1.id = 1;
        item1.date = new Date(2021, 04, 01);
        item1.teacherId = 1;
        item1.auditoryId = 10;
        item1.disciplineId = 100;
        item1.groupId = 1000;
        item1.pairNumber = 1;
        schedule.add(item1);

        ScheduleItem item2 = new ScheduleItem();
        item2.id = 2;
        item2.date = new Date(2021, 04, 01);
        item2.teacherId = 2;
        item2.auditoryId = 20;
        item2.disciplineId = 200;
        item2.groupId = 2000;
        item2.pairNumber = 2;
        schedule.add(item2);

        ScheduleItem item3 = new ScheduleItem();
        item3.id = 3;
        item3.date = new Date(2021, 04, 01);
        item3.teacherId = 3;
        item3.auditoryId = 30;
        item3.disciplineId = 300;
        item3.groupId = 3000;
        item3.pairNumber = 3;
        schedule.add(item3);

        return schedule;
    }
}