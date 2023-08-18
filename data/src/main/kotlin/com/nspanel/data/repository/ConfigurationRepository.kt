package com.nspanel.data.repository

import android.content.Context
import com.nspanel.core.model.PanelConfiguration
import com.nspanel.core.model.PanelConfiguration.GridPanel
import com.nspanel.core.model.PanelConfiguration.HomePanel
import com.nspanel.core.model.SenseConfiguration
import com.nspanel.core.model.SystemConfiguration
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.yaml.snakeyaml.Yaml
import timber.log.Timber
import javax.inject.Inject

typealias YamlMap = Map<String, Any>
typealias YamlList = List<YamlMap>

class ConfigurationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val yamlParser: Yaml
) {

    suspend fun getConfiguration(): SenseConfiguration? = withContext(Dispatchers.IO) {
        runCatching {
            val linkedHashMap =
                yamlParser.load<YamlMap>(context.assets.open(SENSE_CONFIG_FILE))
            val panelSource = linkedHashMap["panelList"] as YamlList
            val systemConfigurationSource = linkedHashMap["systemConfiguration"] as YamlMap

            SenseConfiguration(
                systemConfiguration = systemConfigurationSource.getSystemConfiguration(),
                panelList = panelSource.getPanelList()
            )

        }
            .onFailure(Timber::e)
            .getOrNull()
    }

    private fun YamlMap.getSystemConfiguration(): SystemConfiguration =
        SystemConfiguration(
            mainPanelId = get("mainPanelId") as String?,
            backgroundImageUrl = get("backgroundImageUrl") as String?
        )
    private fun YamlList.getPanelList(): List<PanelConfiguration> =
        mapNotNull { panelObjectMap ->
            when (panelObjectMap["type"]) {
                "grid" -> panelObjectMap.parseGridPanel()
                "home" -> panelObjectMap.parseHomePanel()
                else -> null
            }
        }

    private fun YamlMap.parseHomePanel(): HomePanel = HomePanel(
        id = get("id") as String,
        homeItems = parseItems()
    )

    private fun YamlMap.parseGridPanel(): GridPanel = GridPanel(
        id = get("id") as String,
        columnCount = get("columnCount") as Int,
        gridItems = parseItems()
    )

    private fun YamlMap.parseItems() =
        (get("items") as YamlList).mapNotNull {
            when (it["type"]) {
                "button" -> it.parseGridButtonItem()
                else -> null
            }
        }

    private fun YamlMap.parseGridButtonItem(): PanelConfiguration.PanelItem =
        PanelConfiguration.PanelItem.ButtonItem(
            id = get("id") as String,
            text = get("text") as String,
            icon = get("icon") as String,
            backgroundColor = get("background") as String?,
            entity = get("entity") as String?
        )

    private companion object {
        const val SENSE_CONFIG_FILE = "senseConfig.yml"
    }
}
