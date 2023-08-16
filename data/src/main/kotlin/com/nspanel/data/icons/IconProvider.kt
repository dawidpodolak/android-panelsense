package com.nspanel.data.icons

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nspanel.core.di.DataStoreType
import com.nspanel.core.di.DataStoreType.Type.SvgImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber
import java.io.StringReader
import javax.inject.Inject

class IconProvider @Inject constructor(
    @DataStoreType(SvgImage) private val svgDataStore: DataStore<Preferences>,
) {

    suspend fun loadIconIfNeeded(name: String): String? =
        runCatching {
            val svgPath = downloadSVGPath(name) ?: return@runCatching null

            svgDataStore.edit {
                it[stringPreferencesKey(name)] = svgPath
            }
            return@runCatching svgPath
        }
            .onFailure(Timber::e)
            .getOrNull()

    fun getIcon(context: Context, name: String, color: Int = Color.WHITE): Flow<Drawable> =
        svgDataStore.data.mapNotNull {
            it[stringPreferencesKey(name)] ?: loadIconIfNeeded(name)
        }
            .mapNotNull { svgPath ->
                VectorDrawableCreator.getVectorDrawable(
                    context = context,
                    width = 24.dp,
                    height = 24.dp,
                    viewportWidth = 24f,
                    viewportHeight = 24f,
                    listOf(
                        PathData(
                            svgPath,
                            color
                        )
                    )
                )
            }

    private suspend fun downloadSVGPath(name: String): String? = withContext(Dispatchers.IO) {
        val doc = Jsoup.connect("https://pictogrammers.com/library/mdi/icon/$name").get()
        val className = "IconPreview_icon__VhohC"
        val element = doc.select("div.$className").first()
        val svg = element?.select("svg path")
        svg.toString()

        val xmlPullParser = XmlPullParserFactory.newInstance().newPullParser()
        xmlPullParser.setInput(StringReader(svg.toString()))
        var path = "empty"

        var eventType = xmlPullParser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && xmlPullParser.name == "path") {
                path = xmlPullParser.getAttributeValue(null, "d")
            }
            eventType = xmlPullParser.next()
        }
        return@withContext path
    }
}
