package com.example.providerA.ui

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.providerA.R
import com.example.providerA.databinding.ActivityMainBinding
import com.example.providerA.databinding.AddDialogBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainActivityViewModel>()
    lateinit var userAdapter: UserAdapter
    private lateinit var addDialogBinding: AddDialogBinding


    private val dialog: Dialog by lazy {
        object : Dialog(this) {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                addDialogBinding =
                    DataBindingUtil.inflate(layoutInflater, R.layout.add_dialog, null, false)
                addDialogBinding.apply {
                    dialogHandler = object : OnItemDialogClick {
                        override fun submit(name: String, checked: Boolean) {
                            mainViewModel.insertUser(name, checked)
                            dismiss()
                            this@apply.userNameEditText.setText("")
                            this@apply.checkedCheckBox.isChecked = false
                        }
                    }
                    lifecycleOwner = lifecycleOwner
                    setContentView(root)
                }
                window?.apply {
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setGravity(Gravity.CENTER)
                    setLayout(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.WRAP_CONTENT
                    );
                    attributes = window?.attributes?.apply {
                        flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        userAdapter = UserAdapter(mainViewModel, this)
        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = userAdapter
        }

        mainViewModel.allUsers.observe(this) { userList ->
            userAdapter.differ.submitList(userList)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                dialog.create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}