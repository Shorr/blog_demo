package cn.shorr.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import cn.shorr.widget.CharIndicateView;
import cn.shorr.widget.CharSlideBar;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private List<ContactBean> mContactList;  //联系人集合

    private ListView mContactListView;  //联系人列表
    private CharSlideBar mCharSlideBar;  //字符索引栏
    private CharIndicateView mCharIndicateView;  //字符指示视图
    private ContactlistAdapter mContactListAdapter;  //联系人列表适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化变量
        initVariables();
        //初始化View
        initView();
    }

    /**
     * 初始化变量
     */
    private void initVariables() {
        ContactModel model = new ContactModel();
        mContactList = model.getContactList();
        mContactListAdapter = new ContactlistAdapter(this, mContactList);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mContactListView = (ListView) findViewById(R.id.contact_listview);
        mCharSlideBar = (CharSlideBar) findViewById(R.id.char_slider_bar);
        mCharIndicateView = (CharIndicateView) findViewById(R.id.char_indicate_view);

        //联系人设置适配器
        mContactListView.setAdapter(mContactListAdapter);
        //索引栏和指示视图建立联系
        mCharSlideBar.setupWithIndicateView(mCharIndicateView);
        //设置选中监听事件
        mCharSlideBar.setOnSelectedListener(new CharSlideBar.OnSelectedListener() {
            @Override
            public void onSelected(int position, String selectedChar) {
                Log.e(TAG, "选中--" + selectedChar);
                //根据选中的字符来定位ListView的位置
                locateListViewPositionByChar(selectedChar);
            }
        });
    }

    /**
     * 根据选中的字符来定位ListView的位置
     *
     * @param selectedChar
     */
    private void locateListViewPositionByChar(String selectedChar) {
        //遍历联系人列表找到对应字符的位置
        for (int i = 0; i < mContactList.size(); i++) {
            String nameInitial = mContactList.get(i).getNameInitial();
            if (nameInitial.equals(selectedChar)) {
                //定位ListView的位置
                mContactListView.setSelection(i);
                break;
            }
        }
    }
}
