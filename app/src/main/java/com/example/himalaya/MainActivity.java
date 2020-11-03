package com.example.himalaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.himalaya.adapters.IndicatorAdapter;
import com.example.himalaya.adapters.MainContentAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

public class MainActivity extends FragmentActivity {


    private static final String TAG = "MainActivity";
    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

//        Map<String, String> map = new HashMap<String, String>();
//        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
//            @Override
//            public void onSuccess(CategoryList categoryList) {
//                List<Category> categories = categoryList.getCategories();
//                if (categories != null){
//                    int size = categories.size();
//                    Log.d(TAG, "categories size --- <" + size + ">");
//                    for (Category category : categories) {
//                        Log.d(TAG, "category --->" + category.getCategoryName());
//                    }
//                }
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                LogUtil.d(TAG, message);
//            }
//        });

    }

    private void initView() {
        mMagicIndicator = findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color));

        // 创建indicator适配器
        IndicatorAdapter adapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(adapter);

        // ViewPager
        mContentPager = findViewById(R.id.content_viewpager);

        // 创建内容适配器
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        MainContentAdapter mainContentAdapter = new MainContentAdapter(supportFragmentManager);
        mContentPager.setAdapter(mainContentAdapter);

        // 绑定Viewpager与Indicator
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mContentPager);

    }
}
