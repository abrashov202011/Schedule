package com.example.schedule;

import java.util.Date;
import java.util.List;

/**
 * Реализация логики проверки корректности расписания занятий
 * @author Abrashov
 */
public class ScheduleValidator {

    /**
     * Проверка отсутствия пересечений эелемента расписания занятий item с другими элементами расписания
     * @param schedule Существующее расписание
     * @param id id редактируемой записи
     * @param date дата
     * @param pairNumber номер пары
     * @param auditoryId id аудитории
     * @param teacherId id преподавателя
     * @param id группы
     * @return Возвращает null, если элемент item не пересекается ни с какими другими элементами расписания.
     * Иначе возвращает текст сообщения об ошибке пользователю.
     */
    public static String ValidateItem(List<ScheduleItem> schedule, int id, Date date, int pairNumber, int auditoryId, int teacherId, int groupId) {
        for (int i = 0; i < schedule.size(); i++) {
            ScheduleItem existing = schedule.get(i);
            if (existing.id != id && existing.pairNumber == pairNumber && existing.date.equals(date)) {
                if (existing.auditoryId == auditoryId) {
                    return "Аудитория занята в указанную дату и пару.";
                }
                if (existing.teacherId == teacherId) {
                    return "Преподаватель в указанную дату и пару находится на другой лекции.";
                }
                if (existing.groupId == groupId) {
                    return "Группа в указанную дату и пару находится на другой лекции.";
                }
            }
        }
        return null;
    }

    /**
     * Проверка отсутствия пересечений эелемента расписания занятий item с другими элементами расписания
     * @param schedule Существующее расписание
     * @param item Добавляемый или редактируемый элемент расписания
     * @return Возвращает null, если элемент item не пересекается ни с какими другими элементами расписания.
     * Иначе возвращает текст сообщения об ошибке пользователю.
     */
    public static String ValidateItem(List<ScheduleItem> schedule, ScheduleItem item) {
        return ValidateItem(schedule, item.id, item.date, item.pairNumber, item.auditoryId, item.teacherId, item.groupId);
    }
}
