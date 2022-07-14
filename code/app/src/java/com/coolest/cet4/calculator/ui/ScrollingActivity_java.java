package com.coolest.cet4.calculator.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.coolest.cet4.calculator.MyApplication;
import com.coolest.cet4.calculator.R;
import com.coolest.cet4.calculator.databinding.ActivityScrollingBinding;
import com.coolest.cet4.calculator.utils.BackgroundUtil;
import com.coolest.cet4.calculator.utils.CoolUtils;
import com.coolest.cet4.calculator.utils.java.BackgroundUtil_java;
import com.coolest.cet4.calculator.utils.java.CoolUtils_java;
import com.coolest.cet4.calculator.utils.GithubUpdateUtilsKt;
import com.coolest.cet4.calculator.utils.ReleaseNotesUtil;
import com.coolest.cet4.calculator.utils.ScoreSheetUtil;
import com.coolest.cet4.calculator.utils.ViewUtils_Kotlin;
import com.coolest.cet4.calculator.utils.java.ScoreSheetUtil_java;
import com.coolest.toolbox.utils.BingDailyWallpaperKt;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.DataInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class ScrollingActivity_java extends AppCompatActivity {

	private ActivityScrollingBinding binding;

	//界面变量
	Activity mainActivity = this;
	View mainView;
	CollapsingToolbarLayout toolBarLayout;

	//界面EditText变量
	public EditText listen_A_t;
	public EditText listen_B_t;
	public EditText listen_C_t;
	public EditText reading_A_t;
	public EditText reading_B_t;
	public EditText reading_C_t;
	public static EditText writing_t;
	public static EditText translate_t;
	FloatingActionButton fab_cal;
	FloatingActionButton fab_cls;
	WebView title_web_view;
	WebSettings title_web_view_settings;
	//哼,哼,啊啊啊啊啊
	public MediaPlayer mediaPlayer = null;
	static MenuItem author_b_item = null;

	//工作变量
	double score;
	boolean clear_clicked = false;
	boolean changingTitle = false;
	Queue<String> titleStrQue = new LinkedList<>();
	List<EditText> editTexts = new ArrayList<EditText>();
	String bg_header_URL;
	Thread autoRefreshThread = new Thread(new AutoRefresh());
	//流体背景
	BackgroundUtil_java bgu;

	//工具类创建
	ScoreSheetUtil_java scoreSheetUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		binding = ActivityScrollingBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		Toolbar toolbar = binding.toolbar;
		setSupportActionBar(toolbar);
		toolBarLayout = binding.toolbarLayout;
		toolBarLayout.setTitle(getTitle());

		//设置每日图片
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL wallpaperUrl = new URL(BingDailyWallpaperKt.getTodayWallpaperURL());
					DataInputStream dis = new DataInputStream(wallpaperUrl.openStream());
					Bitmap dailyWallpaper = BitmapFactory.decodeStream(dis);
					MyApplication.setDailyBingPaper(dailyWallpaper);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();


		//检查更新
		try {
			String currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			GithubUpdateUtilsKt.checkUpdate(currentVersion, findViewById(R.id.mainRefreshLayout), "CoolestEnoch", "CetCalculator");
			//判断是否经历了版本更新, 如果更新了就弹出更新日志
			String lastVersion = getSharedPreferences("config", Context.MODE_PRIVATE).getString("last_version", "1.0");
			if (!lastVersion.equals(currentVersion)) {
				getSharedPreferences("config", Context.MODE_PRIVATE).edit().putString("last_version", currentVersion).commit();
				ReleaseNotesUtil.showLastReleaseNotes(mainActivity);
			}
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		//初始化fab_cal
		fab_cal = binding.calcB;
		fab_cls = binding.clsB;

		//工具类创建
		scoreSheetUtil = new ScoreSheetUtil_java();

		//界面变量声明
		listen_A_t = findViewById(R.id.listen_section_A);
		listen_B_t = findViewById(R.id.listen_section_B);
		listen_C_t = findViewById(R.id.listen_section_C);
		reading_A_t = findViewById(R.id.reading_section_A);
		reading_B_t = findViewById(R.id.reading_section_B);
		reading_C_t = findViewById(R.id.reading_section_C);
		writing_t = findViewById(R.id.writing_inp);
		translate_t = findViewById(R.id.translate_inp);
		mainView = getWindow().getDecorView();

		titleStrQue = new LinkedList<>();
		editTexts = new ArrayList<EditText>();

		//哼哼哼啊啊啊啊啊啊啊啊啊
		mediaPlayer = MediaPlayer.create(mainActivity, R.raw.checksum);

		editTexts.add(listen_A_t);
		editTexts.add(listen_B_t);
		editTexts.add(listen_C_t);
		editTexts.add(reading_A_t);
		editTexts.add(reading_B_t);
		editTexts.add(reading_C_t);
		editTexts.add(writing_t);
		editTexts.add(translate_t);

		/**
		 * MIUI小白条沉浸
		 * from https://dev.mi.com/console/doc/detail?pId=2229
		 */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);       //设置沉浸式状态栏，在MIUI系统中，状态栏背景透明。原生系统中，状态栏背景半透明。
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);   //设置沉浸式虚拟键，在MIUI系统中，虚拟键背景透明。原生系统中，虚拟键背景半透明。

		/**
		 * MIUI平行世界适配
		 * from https://dev.mi.com/console/doc/detail?pId=2448
		 */
		String config = this.getApplicationContext().getResources().getConfiguration().toString();
		boolean isInMagicWindow = config.contains("miui-magic-windows");
		if (isInMagicWindow) {
			Snackbar.make(mainView, "当前运行在平行视界下", Snackbar.LENGTH_LONG).show();
		}

		//流体背景
		bgu = new BackgroundUtil_java();
		try {
//			bg_header_URL = "file:///android_asset/hurricane.html";
			int random = (int) (Math.random() * bgu.getLength() * 2) / 2;
			while (bgu.withinPerformanceBlackList(random)) {
				random = (int) (Math.random() * bgu.getLength() * 2) / 2;
			}
			bg_header_URL = bgu.getEle(random);
			title_web_view = (WebView) findViewById(R.id.bg_webview);
			title_web_view_settings = title_web_view.getSettings();
			title_web_view_settings.setJavaScriptEnabled(true);
			//apk内
//			title_web_view.loadUrl("file:///android_asset/index.html");
			title_web_view.loadUrl(bg_header_URL);
			//内部存储
			//title_web_view.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
			//真互联网
			//title_web_view.loadUrl("http://wap.baidu.com");
			if (bg_header_URL.equals("file:///android_asset/index.html")) {
				autoRefreshThread.interrupt();
				autoRefreshThread = new Thread(new AutoRefresh());
				autoRefreshThread.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		fab_cal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				CoolUtils.hideSoftInput(mainActivity, getWindow().getDecorView());
				score = calculateScore(getSharedPreferences("config", Context.MODE_PRIVATE).getString("cet_level", ""));
				if (score >= 0) {
					//整数分数的处理
					String score_s = (String.valueOf(score).contains(".0")) ? String.valueOf(score).substring(0, String.valueOf(score).indexOf('.')) : String.valueOf(score);
					//无限小数分数的处理
					if (score_s.contains(".") && score_s.substring(score_s.indexOf(".") + 1).length() > 2) {
						score_s = score_s.substring(0, score_s.indexOf(".") + 1);
					}
					Snackbar.make(view, score_s + "分," + CoolUtils_java.getEncourageTextByScore(score), Snackbar.LENGTH_LONG)
							.setAction("确定", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									Toast.makeText(mainActivity, "喵", Toast.LENGTH_SHORT).show();
								}
							}).show();
					if (score > 0) {
						changeTitle(score_s + "分," + CoolUtils_java.getEncourageTextByScore(score), 100);
						//旋转滑稽
						//showAnimate(author_b_item);
					}
				} else if (score == -1d) {
					Snackbar.make(getWindow().getDecorView(), "你没有选学段!", Snackbar.LENGTH_LONG).setAction("这就去选", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							select_cet_level(mainActivity);
						}
					}).show();
				}
			}
		});
		fab_cal.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
//				弹出Dialog选择框
//				final String[] items = {"Fluid", "Hurricane", "Windmills", "Colorful", "Waves"};
				final String[] items = bgu.getNameArr();
				AlertDialog.Builder listDialog =
						new AlertDialog.Builder(mainActivity);
				listDialog.setTitle("Choose your background");
				listDialog.setItems(items, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// which 下标从0开始
						int selected;
						if (which == bgu.getEleIdByName("Fluid")) {
							if (!title_web_view.getUrl().equals(bgu.getEle(which))) {
								autoRefreshThread.interrupt();
								autoRefreshThread = new Thread(new AutoRefresh());
								bg_header_URL = bgu.getEle(which);
								title_web_view.clearCache(true);
								title_web_view.loadUrl(bg_header_URL);
								autoRefreshThread.start();
							}
						} else {
							if (!title_web_view.getUrl().equals(bgu.getEle(which))) {
								autoRefreshThread.interrupt();
								bg_header_URL = bgu.getEle(which);
								title_web_view.clearCache(true);
								title_web_view.loadUrl(bg_header_URL);
							}
						}
						if (which == bgu.getEleIdByName("Waves") && !CoolUtils_java.isPad()) {
							Snackbar.make(mainView, "这个背景在手机上可能无法正常显示", Snackbar.LENGTH_LONG).setAction("换掉", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									int random = (int) (Math.random() * bgu.getLength() * 2) / 2;
									while (bgu.withinPerformanceBlackList(random)) {
										random = (int) (Math.random() * bgu.getLength() * 2) / 2;
									}
									bg_header_URL = bgu.getEle(random);
									title_web_view.loadUrl(bg_header_URL);
								}
							}).show();
						} else if (bgu.withinPerformanceBlackList(which)) {
							Snackbar.make(mainView, "手机性能如果不够可能会造成亿点卡顿", Snackbar.LENGTH_LONG).setAction("换掉", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									int random = (int) (Math.random() * bgu.getLength() * 2) / 2;
									while (bgu.withinPerformanceBlackList(random)) {
										random = (int) (Math.random() * bgu.getLength() * 2) / 2;
									}
									bg_header_URL = bgu.getEle(random);
									title_web_view.loadUrl(bg_header_URL);
								}
							}).show();
						} else {
							Snackbar.make(mainView, "Done", Snackbar.LENGTH_LONG).setAction("", null).show();
						}
					}
				});
				listDialog.show();
				return false;
			}
		});

		fab_cls.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				cleaAll();
				changeTitle("清空完成", 100);
				Snackbar.make(view, "完成", Snackbar.LENGTH_LONG)
						.setAction("确定", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(mainActivity, "已经全部清空啦喵", Toast.LENGTH_SHORT).show();
							}
						}).show();
				//旋转滑稽
//				hideAnimate(author_b_item);
			}
		});

		try {
			for (EditText e : editTexts) {
				e.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// 输入的内容变化的监听
						if (!clear_clicked && !toolBarLayout.getTitle().toString().contains("级算分器"))
							changeTitle(getSharedPreferences("config", Context.MODE_PRIVATE).getString("cet_level", "") + "算分器", 100);
//							toolBarLayout.setTitle(getTitle());
						//哼，哼，啊啊啊啊啊啊啊
						if (e.getText().toString().contains("114514")
								|| e.getText().toString().contains("1919810")
								|| toolBarLayout.getTitle().toString().contains("114514")
								|| toolBarLayout.getTitle().toString().contains("1919810")) {
							Toast.makeText(mainActivity, "在粪坑学习的屑", Toast.LENGTH_SHORT).show();
							toilet();
						} else if (mediaPlayer.isPlaying()) {
							mediaPlayer.stop();
							author_b_item.setIcon(R.mipmap.sunglass_huaji);
						}
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});
			}
		} catch (Exception e) {
		}

//		listen_A_t.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mainView = v;
//			}
//		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		toolBarLayout.setTitle(getSharedPreferences("config", Context.MODE_PRIVATE).getString("cet_level", "") + "算分器");
		//问你是几级
		String cet_level = getSharedPreferences("config", Context.MODE_PRIVATE).getString("cet_level", "");
		if (cet_level.equals("")) {
			LinearLayout ll = findViewById(R.id.mainLinerLayout);
			ll.setVisibility(View.GONE);
			MaterialCardView mcv = findViewById(R.id.cet_select_btn);
			mcv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					select_cet_level(mainActivity);
				}
			});
			select_cet_level(this);
		} else {
			MaterialCardView mcv = findViewById(R.id.cet_select_btn);
			mcv.setVisibility(View.GONE);
			findViewById(R.id.mainLinerLayout).setVisibility(View.VISIBLE);
			toolBarLayout.setTitle(cet_level + "算分器");
			if (cet_level.equals("六级")) {
				((TextView) findViewById(R.id.listen_section_A_tip)).setText("1-8题对几个");
				((EditText) findViewById(R.id.listen_section_A)).setHint("输入0-8的整数");
				((TextView) findViewById(R.id.listen_section_B_tip)).setText("9-15题对几个");
				((EditText) findViewById(R.id.listen_section_B)).setHint("输入0-7的整数");
			}
		}
	}

	private void select_cet_level(Context context) {
		LinearLayout cet_level_select_ll = new LinearLayout(context);
		cet_level_select_ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		cet_level_select_ll.setOrientation(LinearLayout.VERTICAL);
		MaterialDialog md = ViewUtils_Kotlin.INSTANCE.getBigCardFromBottom(context, "你是四级还是六级?", cet_level_select_ll);
		List<String> list_cet = new LinkedList<String>();
		list_cet.add("四级");
		SharedPreferences.Editor sp = getSharedPreferences("config", Context.MODE_PRIVATE).edit();
		MaterialCardView cet4 = ViewUtils_Kotlin.INSTANCE.createBigButton(context, null, R.color.light_blue, list_cet, MyApplication.getDailyBingPaper(), R.color.white, new Function0<Unit>() {
			@Override
			public Unit invoke() {
				sp.putString("cet_level", "四级").commit();
				md.cancel();
				onResume();
				return null;
			}
		});

		list_cet.clear();
		list_cet.add("六级");
		MaterialCardView cet6 = ViewUtils_Kotlin.INSTANCE.createBigButton(context, null, R.color.light_blue, list_cet, MyApplication.getDailyBingPaper(), R.color.white, new Function0<Unit>() {
			@Override
			public Unit invoke() {
				sp.putString("cet_level", "六级").commit();
				md.cancel();
				onResume();
				return null;
			}
		});

		cet_level_select_ll.addView(cet4);
		cet_level_select_ll.addView(cet6);
		md.show();
	}

	@SuppressLint("ResourceType")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scrolling, menu);
		//关于作者
		author_b_item = menu.findItem(R.id.author_button);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		/*
		if (id == R.id.action_clear) {
			cleaAll();
			Snackbar.make(mainView, "完成", Snackbar.LENGTH_LONG)
					.setAction("确定", new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(mainActivity, "已经全部清空啦喵", Toast.LENGTH_SHORT).show();
						}
					}).show();
			toolBarLayout.setTitle("清空完成");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			toolBarLayout.setTitle(getTitle());
			return true;
		}
		*/
		if (id == R.id.author_button) {
			/*Snackbar.make(mainView, "作者: Coolest Enoch", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Toast.makeText(this, "作者: Coolest Enoch", Toast.LENGTH_SHORT).show();
			Uri uri = Uri.parse("https://github.com/coolestenoch");
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			intent.setData(uri);
			startActivity(intent);*/
			startActivity(new Intent(this, AboutActivity.class));
		} else if (id == R.id.release_notes) {
			ReleaseNotesUtil.showReleaseNotes(mainActivity);
		}
		return super.onOptionsItemSelected(item);
	}




/*	@Override
	public boolean onOptionsItemLongPressed(MenuItem menuItem){

	}*/

	/**
	 * 摇晃滑稽相关动画代码
	 * 相关文件:
	 * res/anim下两个xml
	 * res/layout/huaji_roll.xml
	 * <p>
	 * 调用方法:
	 * showAnimate(item按钮)
	 * hideAnimate(item按钮)
	 *
	 * @param item
	 */
	private void showAnimate(MenuItem item) {
		hideAnimate(item);
		//这里使用一个ImageView设置成MenuItem的ActionView，这样我们就可以使用这个ImageView显示旋转动画了
		ImageView qrView = (ImageView) getLayoutInflater().inflate(R.layout.huaji_roll, null);
		qrView.setImageResource(R.mipmap.sunglass_huaji);
		item.setActionView(qrView);
		//显示动画
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
		animation.setRepeatMode(Animation.RESTART);
		animation.setRepeatCount(Animation.INFINITE);
		qrView.startAnimation(animation);
	}

	private void hideAnimate(MenuItem menuItem) {
		if (menuItem != null) {
			View view = menuItem.getActionView();
			if (view != null) {
				view.clearAnimation();
				menuItem.setActionView(null);
			}
		}
	}

	void toilet() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 更新UI的操作
				toolBarLayout.setTitle("哼,哼,啊啊啊啊啊");
			}
		});
		mediaPlayer.stop();
		mediaPlayer = MediaPlayer.create(mainActivity, R.raw.checksum);
		mediaPlayer.start();
		author_b_item.setIcon(R.mipmap.ysxb);
	}

	class AutoRefresh implements Runnable {

		@Override
		public void run() {
			try {
				while (true) {
					Thread.sleep(5000);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// 更新UI的操作
							title_web_view.loadUrl(title_web_view.getUrl());
							title_web_view.reload();
						}
					});
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	void cleaAll() {
		new Thread(new ClearAllThread()).start();
	}

	class ClearAllThread implements Runnable {

		@Override
		public void run() {
			CoolUtils.hideSoftInput(mainActivity, getWindow().getDecorView());
			for (EditText e : editTexts) {
				clear_clicked = true;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// 更新UI的操作
						e.setText("");
					}
				});
			}
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			changeTitle(getTitle().toString(), 100);
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					// 更新UI的操作
//					toolBarLayout.setTitle(getTitle());
//				}
//			});
			clear_clicked = false;
		}
	}


	void changeTitle(String inp, int sleepTime) {
		titleStrQue.offer(inp);
		if (!changingTitle) {
			Log.e("Thread", "Start");
			new Thread(new changeTitleClass(sleepTime)).start();
		}
	}

	class changeTitleClass implements Runnable {
		int sleepTime;

		public changeTitleClass(int sleepTime) {
			this.sleepTime = sleepTime;
		}

		@Override
		public void run() {
			changingTitle = true;
			while (!titleStrQue.isEmpty()) {
				changeTitleBase(toolBarLayout.getTitle().toString(), sleepTime, true);
				changeTitleBase(titleStrQue.poll(), 100, false);
			}
			changingTitle = false;
		}
	}

	void changeTitleBase(String inp, int sleepTime, boolean reverse) {
		StringBuffer str = new StringBuffer();
		int i;
		try {
			if (!reverse) {
				for (i = 0; i < inp.length(); i++) {
					str.append(inp.charAt(i));
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// 更新UI的操作
							toolBarLayout.setTitle(str.toString());
						}
					});
					Thread.sleep(sleepTime);
				}
			}
			if (reverse) {
				str.append(inp);
				for (i = inp.length(); i > 0; i--) {
					str.deleteCharAt(i - 1);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// 更新UI的操作
							toolBarLayout.setTitle(str.toString());
						}
					});
					Thread.sleep(sleepTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	double calculateScore(String cet_level) {
		int corrNum = 0;
		double scored = 0.0;
		if (!cet_level.equals("")) {
			try {
				for (EditText e : editTexts) {
					if (e.getText().toString().contains("114514")
							|| e.getText().toString().contains("1919810")
							|| toolBarLayout.getTitle().toString().contains("114514")
							|| toolBarLayout.getTitle().toString().contains("1919810")) {
						toilet();
						Toast.makeText(mainActivity, "这么臭的分数就没必要算了吧", Toast.LENGTH_SHORT).show();
						return -1;
					}
				}
				//听力
				if (!listen_A_t.getText().toString().equals("")) {
					if (cet_level.equals("四级")) {
						if ((int) Double.parseDouble(listen_A_t.getText().toString()) <= 7)
							corrNum += (int) Double.parseDouble(listen_A_t.getText().toString());
						else
							corrNum += 7;
					} else if (cet_level.equals("六级")) {
						if ((int) Double.parseDouble(listen_A_t.getText().toString()) <= 8)
							corrNum += (int) Double.parseDouble(listen_A_t.getText().toString());
						else
							corrNum += 8;
					}
				}
				if (!listen_B_t.getText().toString().equals("")) {
					if (cet_level.equals("四级")) {
						if ((int) Double.parseDouble(listen_B_t.getText().toString()) <= 8)
							corrNum += (int) Double.parseDouble(listen_B_t.getText().toString());
						else
							corrNum += 8;
					} else if (cet_level.equals("六级")) {
						if ((int) Double.parseDouble(listen_B_t.getText().toString()) <= 7)
							corrNum += (int) Double.parseDouble(listen_B_t.getText().toString());
						else
							corrNum += 7;
					}
				}
				if (!listen_C_t.getText().toString().equals("")) {
					if ((int) Double.parseDouble(listen_C_t.getText().toString()) <= 10)
						corrNum += 2 * (int) Double.parseDouble(listen_C_t.getText().toString());
					else
						corrNum += 20;
				}

				if (corrNum < 0) corrNum = 0;
				if (corrNum > 35) corrNum = 35;

				scored += scoreSheetUtil.getLisReadStandardScoreByCorrectNum(corrNum);
				corrNum = 0;

				//阅读
				if (!reading_A_t.getText().toString().equals("")) {
					if (Double.parseDouble(reading_A_t.getText().toString()) <= 10)
						corrNum += Math.round(Double.parseDouble(reading_A_t.getText().toString()) / 2.0);
					else
						corrNum += 5;
				}
				if (!reading_B_t.getText().toString().equals("")) {
					if ((int) Double.parseDouble(reading_B_t.getText().toString()) <= 10)
						corrNum += (int) Double.parseDouble(reading_B_t.getText().toString());
					else
						corrNum += 10;
				}
				if (!reading_C_t.getText().toString().equals("")) {
					int tmp = (int) Double.parseDouble(reading_C_t.getText().toString());
					if (tmp <= 20)
						corrNum = corrNum + tmp * 2;
					else
						corrNum += 20;
//			Toast.makeText(mainActivity, String.valueOf(tmp), Toast.LENGTH_SHORT).show();
				}

				if (corrNum < 0) corrNum = 0;
				if (corrNum > 35) corrNum = 35;
				scored += scoreSheetUtil.getLisReadStandardScoreByCorrectNum(corrNum);

				//写作和翻译
				if (!writing_t.getText().toString().equals("")) {
					scored += scoreSheetUtil.getTraWriStandardScoreByCorrectNum(Integer.parseInt(writing_t.getText().toString()));
				}
				if (!translate_t.getText().toString().equals("")) {
					scored += scoreSheetUtil.getTraWriStandardScoreByCorrectNum(Integer.parseInt(translate_t.getText().toString()));
				}
			} catch (Exception e) {
				Snackbar.make(mainView, "请检查输入，是不是有一栏只输入了一个小数点", Snackbar.LENGTH_LONG).setAction("清除输入错误", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						for (EditText e : editTexts) {
							if (e.getText().toString().startsWith("."))
								e.setText("");
							Snackbar.make(mainView, "清除完成", Snackbar.LENGTH_LONG).setAction("null", null).show();
						}
					}
				}).show();
				scored = -1;
			}
		} else {
			return -1d;
		}

		return scored;
	}

	//旧算法，来自知乎，计算结果和张莉莉上课PPT讲的不一样
	int calculateScore_old() {
		double score_i = 0.0;
		//听力
		if (!listen_A_t.getText().toString().equals("")) {
			score_i += 49.7 * (Integer.parseInt(listen_A_t.getText().toString())) / 7.0;
		}
		if (!listen_B_t.getText().toString().equals("")) {
			score_i += 56.8 * (Integer.parseInt(listen_B_t.getText().toString())) / 7.0;
		}
		if (!listen_C_t.getText().toString().equals("")) {
			score_i += 142.0 * (Integer.parseInt(listen_C_t.getText().toString())) / 7.0;
		}

		//阅读
		if (!reading_A_t.getText().toString().equals("")) {
			score_i += 35.5 * (Integer.parseInt(reading_A_t.getText().toString())) / 10.0;
		}
		if (!reading_B_t.getText().toString().equals("")) {
			score_i += 71.0 * (Integer.parseInt(reading_B_t.getText().toString())) / 10.0;
		}
		if (!reading_C_t.getText().toString().equals("")) {
			score_i += 142.0 * (Integer.parseInt(reading_C_t.getText().toString())) / 10.0;
		}

		//写作和翻译
		if (!writing_t.getText().toString().equals("")) {
			score_i += 106.5 * (Integer.parseInt(writing_t.getText().toString())) / 15.0;
		}
		if (!translate_t.getText().toString().equals("")) {
			score_i += 106.5 * (Integer.parseInt(translate_t.getText().toString())) / 15.0;
		}


		return (int) score_i;

		/*
		String score = "0";
		BigDecimal scoreb = new BigDecimal(Double.valueOf(0.0).toString());
		BigDecimal div = new BigDecimal(Double.valueOf(0.0).toString());
		BigDecimal tmp = null;
		if (!listen_A_t.getText().toString().equals("")) {
			div = new BigDecimal(Double.valueOf(7.0).toString());
			tmp = new BigDecimal(Double.parseDouble(listen_A_t.getText().toString()));
			tmp = tmp.divide(div);
			scoreb.add(tmp);
		}

		score = scoreb.toString();
		return Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(score))));*/
	}

}