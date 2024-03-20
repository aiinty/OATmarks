package com.nikita.oatmarks.screens.search_student

import com.nikita.oatmarks.models.Mark
import com.nikita.oatmarks.models.Subject
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import java.util.*

private const val TAG = "MarksTable"

class MarksTable(private val inputStream: InputStream) {

    fun searchStudentSubjects(surname: String): List<Subject>  {

        var subjects: MutableList<Subject> = mutableListOf()
        //initialize table
        val workbook = XSSFWorkbook(inputStream)
        val sheet = workbook.getSheetAt(0)

        var studentIsFound: Boolean = false

        for (row: Row in sheet) {
            //Found a subject row with our student surname
            if (row.getCell(0).toString().lowercase().split(" ")[0] == surname.lowercase()) {
                if (!studentIsFound) studentIsFound = true

                //Creating subject
                val subject = Subject()
                subject.title = row.getCell(1).toString()
                val marks: MutableList<Mark> = mutableListOf()

                //Reading row at current indexRow in every sheet to make list of all marks of subject for all months
                for (currentMonth in 0 until workbook.numberOfSheets) {
                    val currentSheet = workbook.getSheetAt(currentMonth)
                    //Getting index of the bound for read
                    val lastColumnIndex = getLastColumnIndex(currentSheet.getRow(0))
                    var currentDay = 0
                    //Read all cells in current row of this subject at current sheet
                    for (cell in currentSheet.getRow(row.rowNum)) {
                        //If we read cell at the bound -> stop reading
                        if (cell.columnIndex == lastColumnIndex) {
                            break
                        }
                        //Reading cells from index 2 because 0 is student name, 1 is subject title
                        if (cell.columnIndex >= 2) {
                            currentDay++
                            if (cell.toString().isNotEmpty()){
                                marks += getMarkFromCell(cell, subject.id, currentDay, currentMonth)
                            }
                        }
                    }
                }
                subject.marks = marks
                //Create subject with parameters and add it to subjects
                subjects += subject
            } else if(studentIsFound) {
                break //If we read all rows with student surname, we break reading. So we dont read all table.
            }
        }
        workbook.close()

        return subjects
    }

    //Getting marks from cell
    private fun getMarkFromCell(cell: Cell, subjectId: UUID, day: Int, month: Int): List<Mark> {
        //Creating list of marks, because it can be two marks in one cell
        val result: MutableList<Mark> = mutableListOf(Mark())

        when (cell.cellType) {
            CellType.NUMERIC -> {
                //Converting to int, because double returns
                val markValue = cell.numericCellValue.toInt()
                //If our marks is like, for example, "55" or "24" without space
                if (markValue > 9) {
                    result[0].apply {
                        this.subjectId = subjectId
                        this.value = markValue / 10
                        this.month = month
                        this.day = day
                    }
                    result += Mark()
                    result[1].apply {
                        this.subjectId = subjectId
                        this.value = markValue % 10
                        this.month = month
                        this.day = day
                    }
                } else {
                    result[0].apply {
                        this.subjectId = subjectId
                        this.value = markValue
                        this.month = month
                        this.day = day
                    }
                }
            }
            else -> {
                val markValue = cell.stringCellValue
                if (markValue == "нб" || markValue == "н/б" || markValue == "н\\б" || markValue == "н") {
                    result[0].apply {
                        this.subjectId = subjectId
                        this.month = month
                        this.day = day
                        this.comment = markValue
                    }
                }
                if (markValue.contains(" ")) {
                    var i = 0
                    for (mark in markValue.split(" ")) {
                        if (mark.toIntOrNull() != null) {
                            if (i > 0) {
                                result += Mark()
                            }
                            result[i].apply {
                                this.subjectId = subjectId
                                this.value = mark.toInt()
                                this.month = month
                                this.day = day
                            }
                            i++
                        }
                    }
                }
            }
        }
        return result
    }

    //Cell with "Рубежный контроль" is our bound
    private fun getLastColumnIndex(titleRow: Row): Int {
        for (cell in titleRow) {
            if(cell.toString().contains("Рубежный")) {
                return cell.columnIndex
            }
        }
        return throw java.lang.Exception()
    }
}