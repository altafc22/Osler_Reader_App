package com.altafrazzque.oslerreaderapp.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altafrazzque.oslerreaderapp.databinding.ItemUserStatusBinding
import com.altafrazzque.oslerreaderapp.model.User

class UserStatusListAdapter(private val hoursList: List<User>, private val listener: UserListAdapterListener)
    : RecyclerView.Adapter<UserStatusListAdapter.HoursViewHolder>() {

    private var arrayList: ArrayList<User> = ArrayList()

    fun addData(items:List<User>){
        arrayList.clear()
        arrayList = ArrayList(items)
        notifyDataSetChanged()
    }

    fun clearData(){
        arrayList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val binding = ItemUserStatusBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return HoursViewHolder(binding)
    }

    override fun getItemCount() = hoursList.size

    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        with(holder){
            with(hoursList[position]) {

                binding.tvUserType.text =  if (userStatus.isAdmin == "1") "Admin" else "Operator"
                binding.tvUuid.text =uuid
                binding.tvDeviceId.text = deviceId
                binding.itemCard.setOnClickListener {
                    listener.onItemClick(this)
                }
            }
        }
    }

    inner class HoursViewHolder(val binding: ItemUserStatusBinding) :RecyclerView.ViewHolder(binding.root)

}