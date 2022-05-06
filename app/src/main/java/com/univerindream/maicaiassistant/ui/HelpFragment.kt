package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.univerindream.maicaiassistant.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentHelpBinding.inflate(inflater, container, false)

        binding.wv.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progress.isVisible = newProgress != 100
                binding.progress.progress = newProgress
            }
        }
        binding.wv.settings.javaScriptEnabled = true
        binding.wv.loadUrl("https://github.com/universeindream/MaiCaiAssistant")

        return binding.root

    }

}