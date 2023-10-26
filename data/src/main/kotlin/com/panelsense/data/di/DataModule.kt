package com.panelsense.data.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.panelsense.data.icons.IconProvider
import com.panelsense.data.icons.PictogramIconProvider
import com.panelsense.data.model.MessageType
import com.panelsense.data.mqtt.MqttController
import com.panelsense.data.mqtt.MqttControllerImpl
import com.panelsense.data.serializer.MessageTypeDeserializer
import com.panelsense.data.serializer.MessageTypeSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.yaml.snakeyaml.Yaml
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    fun provideSnakeYaml(): Yaml = Yaml()

    @Provides
    fun provideIconProviderImpl(iconProvider: PictogramIconProvider): IconProvider = iconProvider

    @Provides
    fun providerMqttControllerImpl(mqttController: MqttControllerImpl): MqttController =
        mqttController

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .disableHtmlEscaping()
        .registerTypeAdapter(MessageType::class.java, MessageTypeSerializer())
        .registerTypeAdapter(MessageType::class.java, MessageTypeDeserializer())
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
}
