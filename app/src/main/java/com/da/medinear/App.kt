package com.da.medinear

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.da.medinear.model.Clinic
import com.da.medinear.utils.Constance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class App : Application(), ValueEventListener {

    var data = MutableLiveData<ArrayList<Clinic>>()

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().getReference(Constance.FIREBASE_CLINIC).addValueEventListener(this)
    }

    override fun onDataChange(snapshot: DataSnapshot) {
        val data = arrayListOf<Clinic>()
        snapshot.children.forEach {
            it.getValue(Clinic::class.java)?.apply {
                data.add(this)
            }
        }
        this.data.postValue(data)
    }

    override fun onCancelled(error: DatabaseError) {
    }

}