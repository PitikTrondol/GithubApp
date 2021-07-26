package com.mobile.android.dependencies.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobile.android.dependencies.logging.Trace
import com.mobile.android.feature.contributor.model.ContributorEntity
import java.lang.reflect.Type

class ReposConverter {
    @TypeConverter
    fun stringToIntList(data: String?): List<Int>? {
        return data?.let {stringData->
            stringData.split(",").map {
                it.toIntOrNull()
            }
        }?.filterNotNull()
    }

    @TypeConverter
    fun intListToString(ints: List<Int>?): String? {
        return ints?.joinToString(",")
    }
}