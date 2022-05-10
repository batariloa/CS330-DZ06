package com.example.cs330_dz06.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs330_dz06.R
import com.example.cs330_dz06.data.model.Exam
import com.example.cs330_dz06.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        setContentView(binding.root)
        setupListofDataIntoRecyclerView()


        binding.btnAddExam.setOnClickListener {

            if (binding.etNameAdd.text.isNotEmpty() && binding.etIndexAdd.text.isNotEmpty()) {
                val exam = Exam(
                    1,
                    binding.etNameAdd.text.toString(),
                    binding.etNameAdd.text.toString(),
                )

                if (viewModel.addExam(exam) > -1) {
                    Toast.makeText(applicationContext, "Exam added", Toast.LENGTH_SHORT)

                    binding.etNameAdd.text.clear()
                    binding.etIndexAdd.text.clear()
                    setupListofDataIntoRecyclerView()
                } else
                    Toast.makeText(applicationContext, "Error adding the exam", Toast.LENGTH_SHORT)

            }
            else{
                Toast.makeText(applicationContext, "You must fill all the data.", Toast.LENGTH_SHORT)

            }
        }


    }
     fun setupListofDataIntoRecyclerView() {

        val itemsList = binding.rvItems
        if (viewModel.getItemsList().isNotEmpty()) {

           itemsList.visibility = View.VISIBLE

            // Set the LayoutManager that this RecyclerView will use.
            itemsList.layoutManager = LinearLayoutManager(this)
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(this, viewModel.getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            itemsList.adapter = itemAdapter
        } else {

            itemsList.visibility = View.GONE

        }
    }
    fun updateRecordDialog(exam: Exam) {
        val updateDialog = Dialog(this)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.findViewById<EditText>(R.id.et_name).setText(exam.name)
        updateDialog.findViewById<EditText>(R.id.edit_code).setText(exam.code)

        updateDialog.findViewById<Button>(R.id.btn_accept_edit).setOnClickListener{

            val name = updateDialog.findViewById<EditText>(R.id.et_name).text.toString()
            val email = updateDialog.findViewById<EditText>(R.id.edit_code).text.toString()



            if (!name.isEmpty() && !email.isEmpty()) {
                val status =
                    viewModel.databaseHandler.updateStudent(Exam(exam.id, name, email))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Exam updated.", Toast.LENGTH_LONG).show()

                    setupListofDataIntoRecyclerView()

                    updateDialog.dismiss() // Dialog will be dismissed
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Name or exam code cannot be blank",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        updateDialog.findViewById<Button>(R.id.btn_cancel_edit).setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        //Start the dialog and display it on screen.
        updateDialog.show()
    }


}