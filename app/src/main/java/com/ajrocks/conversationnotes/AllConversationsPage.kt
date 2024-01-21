package com.ajrocks.conversationnotes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajrocks.conversationnotes.databinding.ActivityAllConversationsPageBinding
import com.ajrocks.conversationnotes.login.api.impl.LoginApiImpl
import com.ajrocks.conversationnotes.login.api.interfaces.LoginRequestPayload
import com.ajrocks.conversationnotes.utils.CustomRVAdapter
import com.ajrocks.conversationnotes.utils.CustomView
import com.ajrocks.conversationnotes.viewmodels.AllConversationsPageVM
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class AllConversationsPage : AppCompatActivity() {
    private lateinit var activityAllConversationsPage: ActivityAllConversationsPageBinding
    private lateinit var topBar: MaterialToolbar
    private lateinit var sortChips: MutableList<Chip>
    private lateinit var allConversationsRV: RecyclerView
    private var allConversationsPageViewModel = AllConversationsPageVM()
    private var listItems: MutableList<CustomView> = MutableList(100) {
        CustomView.DateView("$it Dec 2023")
    }

    @Inject lateinit var loginApi: LoginApiImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        activityAllConversationsPage =
            ActivityAllConversationsPageBinding.inflate(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)
        setContentView(activityAllConversationsPage.root)

        topBar = activityAllConversationsPage.topAppBar
        topBar.menu.getItem(0).setOnMenuItemClickListener {
            allConversationsPageViewModel.logout(this)
            true
        }

        sortChips = MutableList(2) {
            when (it) {
                0 -> activityAllConversationsPage.sortAllOption
                1 -> activityAllConversationsPage.sortMostRecentOption
                else -> activityAllConversationsPage.sortAllOption
            }
        }

        sortChips.forEach {
            it.setOnClickListener { _ ->

                allConversationsPageViewModel.onSortOptionSelected(
                    allConversationsPageViewModel.indexToSortOption(
                        sortChips.indexOfFirst{chipToCompare ->
                            chipToCompare == it
                        }
                    )
                )

                sortChips.forEach { chipIt ->
                   chipIt.isChecked = false
                }
                it.isChecked = true
            }
        }

        allConversationsRV = activityAllConversationsPage.allConversationsRv

        allConversationsRV.layoutManager = LinearLayoutManager(this)
        allConversationsRV.adapter = CustomRVAdapter(listItems)


//        runBlocking {
////            val data = loginApi.login(LoginRequestPayload("07abhishekrock@gmail.com"))
////            Log.d("AllConversationsPage", data.username)
//        }


    }
}