package com.tan.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import cn.tan.lib.base.BaseActivity;
import cn.tan.lib.util.ImageLoaderUtil;
import cn.tan.lib.util.ImageUtil;
import cn.tan.lib.util.IntentUtil;

public class MainActivity extends BaseActivity {

    public final String[] URLS = new String[]{
            "http://cdn.eyeem.com/thumb/h/60/8ee5cf9a90679bd75342a049cc00ae00b3447ecf-1433913412",
            "http://cdn.eyeem.com/thumb/h/60/3f08061c364c9c7f42bfc80d353178af79d6b969-1433915818",
            "http://cdn.eyeem.com/thumb/h/60/24d8afdf2ab0639254d78c04c325bdbdb5273339-1433913746",
            "http://cdn.eyeem.com/thumb/h/60/70346c285bd09e88c0228558e7193993f4b50bc2-1433915540",
            "http://cdn.eyeem.com/thumb/h/60/7dc5acc051ed6d51e3d3a89ecbb17b85510f024f-1433912472",
            "http://cdn.eyeem.com/thumb/h/60/83bb3ec9591efbb3107cc7170a71d5d52d9e0d7a-1433916083",
            "http://cdn.eyeem.com/thumb/h/60/357c41ccb919327fde2e0e15373dfdc9912bc03c-1433914225",
            "http://cdn.eyeem.com/thumb/h/60/ec51885100ea56b6a2688cc54ba1035ddde264d0-1433909996",
            "http://cdn.eyeem.com/thumb/h/60/76423f0e8e54de66598dc5cf01c269542c77cca7-1433912605",
            "http://cdn.eyeem.com/thumb/h/60/aeaad3dd25c896c45b05c4261cd453048ee1c8df-1433915132",
            "http://cdn.eyeem.com/thumb/h/60/a4b435512c04ada13c99396ddda4cc9c4f898837-1433911172",
            "http://cdn.eyeem.com/thumb/h/60/a1e082fb60fe554f436ed2ffe4cf15a13b0f736d-1433913678",
            "http://cdn.eyeem.com/thumb/h/60/199a8eee084e7e40db3721301346b1bc6b6f6ea0-1433912099",
            "http://cdn.eyeem.com/thumb/h/60/009dd017e8016fa82daf67302bd962e50ee06cbe-1433913912",
            "http://cdn.eyeem.com/thumb/h/60/76eeaa60272bde29e792eac91056598a3ce4b04e-1433915547",
            "http://cdn.eyeem.com/thumb/h/60/c781d3a96c0053b4f82c8942259cd46d03b3256a-1433914425",
            "http://cdn.eyeem.com/thumb/h/60/7af4d496ea8dd379921df0cb6519a4907cb66c45-1433913002",
            "http://cdn.eyeem.com/thumb/h/60/7b83046a3b92bd7c6d6c3fb06c2d77cad670f7cd-1433914299",
            "http://cdn.eyeem.com/thumb/h/60/bfca2e78e40017c8076f2e687d2feff17abb921e-1433915843",
            "http://cdn.eyeem.com/thumb/h/60/a2607e70f2047e35475f05b46897f26d69b5f62-1433915320",
            "http://cdn.eyeem.com/thumb/h/60/d594c7b3815de3310487e77cbdacdf6bcc269bfd-1433915745",
            "http://cdn.eyeem.com/thumb/h/60/a5c7fa4edecf28a8ccd161601cf7e73b24d3f5b8-1433912689",
            "http://cdn.eyeem.com/thumb/h/60/688c2b8078c46c2b41ce7c21f7917d0b47a51935-1433905439",
            "http://cdn.eyeem.com/thumb/h/60/209b4cb4af1dd8ad63b2111cb696a64e8e42c301-1433914870",
            "http://cdn.eyeem.com/thumb/h/60/1be66bd0fcebda8ca5ea24f108626c58b85bd5c9-1433914377",
            "http://cdn.eyeem.com/thumb/h/60/833c1c0a8bdf70df66996b8a075e316ec5176635-1433915465",
            "http://cdn.eyeem.com/thumb/h/60/c8006e6510a8acac3ff2ec3e8594d0800286d614-1433904782",
            "http://cdn.eyeem.com/thumb/h/60/4cab1f9c1a3553096147968c7910257fe4ec5202-1433913880",
            "http://cdn.eyeem.com/thumb/h/60/a7055693096977bbcf30640dc50764522b9c0c51-143391602",
            "http://cdn.eyeem.com/thumb/h/60/09cd749df746d876aa396b60d1dce08e0aefc4dc-1433915632",
            "http://cdn.eyeem.com/thumb/h/60/f978dff44c76190055f964aedd05dd196f00dcba-1426221883",
            "http://cdn.eyeem.com/thumb/h/60/f0dc7e3274bf3c1d71137a726a83ad7884d691cb-1426220990",
            "http://cdn.eyeem.com/thumb/h/60/eb58c8a9d85756a57d0249c381452516ea1d5bee-1426221910",
            "http://cdn.eyeem.com/thumb/h/60/eaebeac69797df3e05a2143fa2239969028b6f6c-1426222680",
            "http://cdn.eyeem.com/thumb/h/60/e8f92eb8a5df92d4f583b54c46753d6bdc2d4140-1426221363",
            "http://cdn.eyeem.com/thumb/h/60/e3b74d48d97c94ed1b0245378387f89441d71a74-1426212254",
            "http://cdn.eyeem.com/thumb/h/60/de5eb9492248443925e250663c7bdad8bba38633-1426217057",
            "http://cdn.eyeem.com/thumb/h/60/d59935cc46cb45d87360996d516e2d1dc03ad287-1426217867",
            "http://cdn.eyeem.com/thumb/h/60/d4ca440942ad841500f8d2f883abe23adfdeebea-1426219560",
            "http://cdn.eyeem.com/thumb/h/60/cf9b97401f54bb04cc7fad62451aa0b54e3d8688-1426221348",
            "http://cdn.eyeem.com/thumb/h/60/ce6fc8bf60181570f24d27735395cc6e2c544ada-1426218275",
            "http://cdn.eyeem.com/thumb/h/60/cd4d3bf9edfe11da3d42e8d79c70846817c30a3a-1426216120",
            "http://cdn.eyeem.com/thumb/h/60/c94db98fefccc758f648469fb05e937d7c721fe0-1426214938",
            "http://cdn.eyeem.com/thumb/h/60/c8f49a3a01d9ffaac1f5f0e02626db078d83826b-1426211287",
            "http://cdn.eyeem.com/thumb/h/60/c2334a2fb7f42c6cf7a9d6dece0f815ebd889be1-1426220639",
            "http://cdn.eyeem.com/thumb/h/60/ae36d3b2d1c72d6ef3f08ed0a7aa5c2a22724697-1426218571",
            "http://cdn.eyeem.com/thumb/h/60/acc6050c37b19c53fd024b49637c8a1eeefe1f48-1426220566",
            "http://cdn.eyeem.com/thumb/h/60/aa7e0d28dec4120f43aa91db34fcf885671be488-1426221342",
            "http://cdn.eyeem.com/thumb/h/60/9f9381aaa37ad42b8f137d9f2bd1499ea86aab59-1426220547",
            "http://cdn.eyeem.com/thumb/h/60/89014e4c5368f40be5d3fcd0add9a73f5469bad0-1426221714",
            "http://cdn.eyeem.com/thumb/h/60/87b58f743ca363aad996b0ba631e4c21c9d6c54e-1426220290",
            "http://cdn.eyeem.com/thumb/h/60/863ea3855f58e7345af1760fc7dd35f526412930-1426220834",
            "http://cdn.eyeem.com/thumb/h/60/85821346a53a6edc8d9cdacb3f3b96904decfc61-1426219977",
            "http://cdn.eyeem.com/thumb/h/60/7f6484eb5a5867e8a9502d224e30ba3e7c36b52e-1426221533",
            "http://cdn.eyeem.com/thumb/h/60/7eb8fa379a7c8cdf7673dd7a0a9671f78dbd6f80-1426220886",
            "http://cdn.eyeem.com/thumb/h/60/7d7df5f3247f3823daa365142414a9c8bafc0c95-1426221877",
            "http://cdn.eyeem.com/thumb/h/60/7388c9db2f18f4629390fd0cdbc4f2d1e8581338-1426221585",
            "http://cdn.eyeem.com/thumb/h/60/722392a8e942767939e27b967e8cb24802c486d9-1426215966",
            "http://cdn.eyeem.com/thumb/h/60/70db64232168b6703615c550682e280ecd3dfb7d-1426220112",
            "http://cdn.eyeem.com/thumb/h/60/6bdf34696c240e748807557ac36596670cdc4d99-1426220339",
            "http://cdn.eyeem.com/thumb/h/60/67b0b4870edbc7974f4fa0b7d0b28b36eaf36d46-1426205503",
            "http://cdn.eyeem.com/thumb/h/60/668d3e5c081ee460d3f67d4a237716db88385a24-1426217583",
            "http://cdn.eyeem.com/thumb/h/60/5d227036c0556259535d2590dd9ebfaa86835dda-1426216818",
            "http://cdn.eyeem.com/thumb/h/60/5637b468d431d7357f2ef7713d2353a62aef926c-1426220233",
            "http://cdn.eyeem.com/thumb/h/60/543aa8725eb65393351bd186da4e46eb52ae8b2f-1426219610",
            "http://cdn.eyeem.com/thumb/h/60/50f802c312ae119c52a5449ca2e4dee18935e13c-1426222731",
            "http://cdn.eyeem.com/thumb/h/60/4bba49fe461496c795c7aaeed1302bfc46c97963-1426220833",
            "http://cdn.eyeem.com/thumb/h/60/4b04d7db2673dea9c122103f4812806023fd99bf-1426219122",
            "http://cdn.eyeem.com/thumb/h/60/494b7581b95a6b41a586cfc558251284836cccfc-1426219578",
            "http://cdn.eyeem.com/thumb/h/60/3529de2886b38507cc1f16bd72baaab29e0c6246-1426222265",
            "http://cdn.eyeem.com/thumb/h/60/32e9b00e6d46f7157b07706e1f0a2ea01507f257-1426210938",
            "http://cdn.eyeem.com/thumb/h/60/3143df51426a207b326ce9371045952268727752-1426222675",
            "http://cdn.eyeem.com/thumb/h/60/304ce8142fdbeae731c7dca33ff231faf4237b54-1426220803",
            "http://cdn.eyeem.com/thumb/h/60/22e39bff1657b760ec3ac58e1c6a70890d2a9efe-1426221057",
            "http://cdn.eyeem.com/thumb/h/60/1894c25b50e6c72dab6e1c498e4eee4df6a27fe7-1426218706",
            "http://cdn.eyeem.com/thumb/h/60/170eaf0a73c377361ce21e093e17ac102973aa0d-1426222442",
            "http://cdn.eyeem.com/thumb/h/60/14d974f7fdb5d2ad946bef4e0aac6f872fe846c6-1426221150",
            "http://cdn.eyeem.com/thumb/h/60/0a27bb06da01a1c9e79b474bfd4fcbe11980c7df-1426220721",
            "http://cdn.eyeem.com/thumb/h/60/088705c6e4b2b78a75ac131844883909ce5d02c5-1426222771",
            "http://cdn.eyeem.com/thumb/h/60/020df6440692a0dcc2aece0bf829761dace23455-1426216257"
    };
    private int themeRes = R.style.Theme_Basic_Night;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toorBar("");
        getView(R.id.btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

//        RecyclerView mRecyclerView=getView(R.id.my_recycler_view);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(new HomeAdapter());
//        ListView listView= (ListView) findViewById(R.id.my_recycler_view);
//        listView.setAdapter(new MyAdapter());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (IntentUtil.REQUEST_CODE_GETIMAGE_BYSDCARD == requestCode) {
            String file = ImageUtil.getAbsoluteImagePath(data.getData(), context);
            ImageView imageView = getView(R.id.image);
            imageView.setImageBitmap(ImageUtil.getBitmapFromFile(file, this));
        }
    }

    public class MyAdapter extends BaseAdapter {

        public int getCount() {
            return URLS.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                LinearLayout linearLayout = new LinearLayout(MainActivity.this);
                convertView = linearLayout;
                viewHolder = new ViewHolder();
                ImageView imageView = new ImageView(MainActivity.this);
                linearLayout.addView(imageView);
                viewHolder.iv = imageView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(URLS[position], viewHolder.iv, ImageLoaderUtil.getInstance().getImageOptions());
            return convertView;
        }

        public final class ViewHolder {
            public ImageView iv;
        }
    }

//    public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            ImageView imageView = new ImageView(context);
//            MyViewHolder holder = new MyViewHolder(imageView);
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//            ImageLoader.getInstance().displayImage(URLS[position], holder.tv, ImageLoaderUtil.getInstance().getImageOptions());
//        }
//
//        @Override
//        public int getItemCount() {
//            return URLS.length;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            ImageView tv;
//
//            public MyViewHolder(View view) {
//                super(view);
//                tv = (ImageView) view;
//                tv.setMaxHeight(300);
//                tv.setMaxWidth(400);
//            }
//        }
//    }
}
