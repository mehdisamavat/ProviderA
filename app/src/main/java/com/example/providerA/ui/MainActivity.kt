package com.example.providerA.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.providerA.R
import com.example.providerA.databinding.ActivityMainBinding
import com.example.providerA.ui.adapter.UserAdapter
import com.example.providerA.ui.dialog.AddDialog
import com.example.providerA.ui.dialog.OnSubmitDialogClick
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    lateinit var userAdapter: UserAdapter

    private val addDialog: AddDialog by lazy { AddDialog(this,object : OnSubmitDialogClick {
        override fun onSubmit(name: String, checked: Boolean) {
            mainViewModel.insertUser(name, checked)
            addDialog.dismiss()
        }
    }) }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        userAdapter = UserAdapter(mainViewModel, this)
        binding.itemRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            addItemDecoration( DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = userAdapter
        }

        mainViewModel.allUsers.observe(this) { userList ->
            userAdapter.differ.submitList(userList)
        }

        mainViewModel.stateResponse.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                addDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}