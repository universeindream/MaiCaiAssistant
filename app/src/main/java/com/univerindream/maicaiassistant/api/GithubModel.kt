package com.univerindream.maicaiassistant.api

data class GithubReleases(val name: String, val tag_name: String)

data class GithubContents(val name: String, val path: String, val url: String, val download_url: String, val type: String)