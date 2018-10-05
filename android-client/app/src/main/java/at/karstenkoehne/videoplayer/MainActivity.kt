package at.karstenkoehne.videoplayer

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.VideoView



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val video = findViewById(R.id.videoView) as VideoView
        video.setVideoURI(Uri.parse("https://streamsrv55.feratel.co.at/streams/1/05131_5bb74f46-7f3cVid.mp4"));
        video.start()
    }
}
