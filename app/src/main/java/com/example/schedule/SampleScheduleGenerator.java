package com.example.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Реализация генерации случайного расписания занятий (для отладки программы)
 * @author Abrashov
 */
public class SampleScheduleGenerator {

    static final Random rnd= new Random();

    /**
     * Генерация случайного расписания занятий
     * @param groups список групп
     * @param disciplines список дисциплин
     * @param teachers список преподавателей
     * @param auditories список аудиторий
     * @param startDate дата начала периода
     * @param endDate дата окончания периода
     */
    public static ArrayList<ScheduleItem> genetateSampleSchedule(List<SimpleItem> groups,
                 List<SimpleItem> disciplines, List<SimpleItem> teachers, List<SimpleItem> auditories,
                 Date startDate, Date endDate) {
        ArrayList<ScheduleItem> schedule = new ArrayList<ScheduleItem>();
        Date date = startDate;
        Calendar calendar = Calendar.getInstance();
        int id = 1;
        while (!date.equals(endDate)) {
            ArrayList<ScheduleItem> scheduleForDay = new ArrayList<ScheduleItem>();
            for (SimpleItem group : groups) {
                for (int pair = 1; pair <= 3; pair++) {
                    ScheduleItem item = new ScheduleItem();
                    item.id = id;
                    item.date = (Date) date.clone();
                    item.pairNumber = pair;
                    item.groupId = group.id;
                    item.disciplineId = getRandomItem(disciplines).id;

                    while (true) {
                        item.teacherId = getRandomItem(teachers).id;
                        item.auditoryId = getRandomItem(auditories).id;
                        String msg = ScheduleValidator.ValidateItem(scheduleForDay, item);
                        if (msg == null) {
                            break;
                        }
                    }

                    scheduleForDay.add(item);
                    id++;
                }
            }
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            date = calendar.getTime();
            schedule.addAll(scheduleForDay);
        }
        return schedule;
    }

    static SimpleItem getRandomItem(List<SimpleItem> items) {
        return items.get(rnd.nextInt(items.size()));
    }

    /**
     * Генерация списка групп
     * @return
     */
    public static ArrayList<SimpleItem> generateGroups(){
        ArrayList<SimpleItem> items = new ArrayList<SimpleItem>();
        items.add(new SimpleItem(0, "ПИНз-117"));
        items.add(new SimpleItem(0, "ПИНз-118"));
        items.add(new SimpleItem(0, "ПИНз-119"));
        return items;
    }

    /**
     * Генерация списка дисциплин
     * @return
     */
    public  static ArrayList<SimpleItem> generateDisciplines() {
        ArrayList<SimpleItem> items = new ArrayList<SimpleItem>();
        items.add(new SimpleItem(0, "Мобильные ОС"));
        items.add(new SimpleItem(0, "Кроссплатформенное ПО"));
        items.add(new SimpleItem(0, "Проектирование ИС"));
        items.add(new SimpleItem(0, "Базы данных"));
        items.add(new SimpleItem(0, "Дискретная математика"));
        items.add(new SimpleItem(0, "ООП"));
        items.add(new SimpleItem(0, "Информационная безопасность"));
        return items;
    }

    /**
     * Генерация списка преподавателей
     * @return
     */
    public  static ArrayList<SimpleItem> generateTeachers(){
        ArrayList<SimpleItem> items = new ArrayList<SimpleItem>();
        items.add(new SimpleItem(0, "Кульков Я.Ю."));
        items.add(new SimpleItem(0, "Быков А.А."));
        items.add(new SimpleItem(0, "Привезенцев Д.Г."));
        items.add(new SimpleItem(0, "Данилин С.Н."));
        items.add(new SimpleItem(0, "Астафьев А.В."));
        items.add(new SimpleItem(0, "Белякова А.С."));
        items.add(new SimpleItem(0, "Захаров А.А."));
        return items;
    }

    /**
     * Генерация списка аудиторий
     * @return
     */
    public static ArrayList<SimpleItem> generateAuditories() {
        ArrayList<SimpleItem> items = new ArrayList<SimpleItem>();
        items.add(new SimpleItem(0, "410"));
        items.add(new SimpleItem(0, "413"));
        items.add(new SimpleItem(0, "417"));
        items.add(new SimpleItem(0, "315"));
        items.add(new SimpleItem(0, "313"));
        items.add(new SimpleItem(0, "217"));
        return items;
    }
}
