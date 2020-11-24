package com.yu.demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yu.emergetextview.EmergeTextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var poem: Array<String> = arrayOf(
        "南风知我意，吹梦入西州。",
        "鹅，鹅，鹅，\n曲项向天歌。\n白毛浮绿水，\n红掌拨清波。",
        "I just wish someday and somehow，We can be back together， Together we'll stay，Always and forever。",
        "大鹏一日同风起，扶摇直上九万里。假令风歇时下来，犹能簸却沧溟水。世人见我恒殊调，闻余大言皆冷笑。宣父犹能畏后生，丈夫未可轻年少。"
    )
    var clickTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rg_type.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rb_type_typer -> view_emerge_text.setType(EmergeTextView.EmergeType.typer)
                R.id.rb_type_random -> view_emerge_text.setType(EmergeTextView.EmergeType.randrom)
            }
            view_emerge_text.start()
        }
        btn_change.setOnClickListener {
            var str = poem[clickTime%4]
            view_emerge_text.setText(str)
            view_emerge_text.start()
            clickTime++
        }
    }
}
