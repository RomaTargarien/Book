package com.example.photogallery

import android.content.Context
import android.preference.PreferenceManager
import androidx.core.content.edit

private const val PREF_LAST_RESULT_ID = "lastResultId"
private const val PREF_SEARCH_QUERY = "searchQuery"

object QuerryPreferences {
    fun getSoredQuerry(context: Context): String{
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY,"")!!
    }
    fun setStoredQuerry(context: Context, query: String){
        PreferenceManager.getDefaultSharedPreferences(context).edit{putString(PREF_SEARCH_QUERY,query)}
    }
    fun getLastResultId(context: Context):String{
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LAST_RESULT_ID,"")!!
    }
    fun setLastResultId(context: Context,lastResultId: String){
        PreferenceManager.getDefaultSharedPreferences(context).edit{
            putString(PREF_LAST_RESULT_ID,lastResultId)
        }
    }
}