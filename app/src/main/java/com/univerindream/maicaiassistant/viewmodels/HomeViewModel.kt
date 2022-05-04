package com.univerindream.maicaiassistant.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.blankj.utilcode.util.TimeUtils
import com.univerindream.maicaiassistant.MCUtil
import com.univerindream.maicaiassistant.api.GithubReleases
import com.univerindream.maicaiassistant.data.DataRepository
import com.univerindream.maicaiassistant.data.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val githubRepository: GithubRepository
) : ViewModel() {

    private val _timerStatus: MutableLiveData<Boolean> by lazy {
        MutableLiveData(dataRepository.timerStatus)
    }
    private val _timerTime: MutableLiveData<Long> by lazy {
        MutableLiveData(dataRepository.timerTime)
    }

    val timerStatus: LiveData<Boolean>
        get() = _timerStatus
    val timerTime: LiveData<Long>
        get() = _timerTime

    val hasAssistantPermission: LiveData<Boolean>
        get() = MutableLiveData(MCUtil.hasServicePermission())

    val latestReleases: LiveData<Result<List<GithubReleases>>>
        get() = githubRepository.latestReleases().asLiveData()

    init {
        if (dataRepository.timerStatus && dataRepository.timerTime > System.currentTimeMillis()) {
            MCUtil.enableAlarm(dataRepository.timerTime)
        } else {
            dataRepository.timerStatus = false
            _timerStatus.value = false
        }
    }

    private fun getFutureTimerTime(): Long {
        val timerTime = dataRepository.timerTime
        if (timerTime > System.currentTimeMillis()) return timerTime

        val hms = TimeUtils.millis2String(timerTime, "HH:mm").split(":")
        val hour = hms.getOrNull(0)?.ifBlank { "5" }?.toIntOrNull() ?: 5
        val minute = hms.getOrNull(1)?.ifBlank { "55" }?.toIntOrNull() ?: 55
        return MCUtil.calcNextTime(hour, minute)
    }

    fun setTimerTime(hour: Int, minute: Int) {
        val nextTime = MCUtil.calcNextTime(hour, minute)
        dataRepository.timerTime = nextTime
        _timerTime.value = nextTime

        if (dataRepository.timerStatus) MCUtil.setTimerAlarm(true, nextTime)
    }

    fun setTimerStatus(status: Boolean) {
        dataRepository.timerStatus = status
        _timerStatus.value = status

        val nextTime = getFutureTimerTime()
        dataRepository.timerTime = nextTime
        _timerTime.value = nextTime
        MCUtil.setTimerAlarm(status, nextTime)
    }

    fun updateTimerStatus() {
        _timerStatus.value = dataRepository.timerStatus
    }
}