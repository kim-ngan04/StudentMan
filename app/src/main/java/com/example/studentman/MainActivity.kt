package com.example.studentman

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )

    private lateinit var studentAdapter: StudentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentAdapter = StudentAdapter(students, ::onEditClick, ::onDeleteClick)

        findViewById<RecyclerView>(R.id.recycler_view_students).apply {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun onEditClick(position: Int) {
        val student = students[position]
        showEditStudentDialog(student, position)
    }

    private fun onDeleteClick(position: Int) {
        val studentToDelete = students[position]

        AlertDialog.Builder(this)
            .setMessage("Do you really want to delete ${studentToDelete.studentName}?")
            .setPositiveButton("Yes") { _, _ ->
                deleteStudent(position, studentToDelete)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteStudent(position: Int, studentToDelete: StudentModel) {
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        Snackbar.make(findViewById(R.id.recycler_view_students), "${studentToDelete.studentName} has been deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                students.add(position, studentToDelete)
                studentAdapter.notifyItemInserted(position)
            }
            .show()
    }

    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val studentNameEditText = dialogView.findViewById<android.widget.EditText>(R.id.edit_student_name)
        val studentIdEditText = dialogView.findViewById<android.widget.EditText>(R.id.edit_student_id)

        AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val newStudent = StudentModel(studentNameEditText.text.toString(), studentIdEditText.text.toString())
                students.add(newStudent)
                studentAdapter.notifyItemInserted(students.size - 1)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditStudentDialog(student: StudentModel, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val studentNameEditText = dialogView.findViewById<android.widget.EditText>(R.id.edit_student_name)
        val studentIdEditText = dialogView.findViewById<android.widget.EditText>(R.id.edit_student_id)

        studentNameEditText.setText(student.studentName)
        studentIdEditText.setText(student.studentId)

        AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                student.studentName = studentNameEditText.text.toString()
                student.studentId = studentIdEditText.text.toString()
                studentAdapter.notifyItemChanged(position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
