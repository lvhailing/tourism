package com.tourism.my.tourismmanagement.db.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.tourism.my.tourismmanagement.db.db.afinal.FinalDb;
import com.tourism.my.tourismmanagement.db.db.afinal.db.sqlite.SqlInfo;
import com.tourism.my.tourismmanagement.db.db.model.Me;
import com.tourism.my.tourismmanagement.db.db.model.User;

import java.util.LinkedList;
import java.util.List;

public class DBManager {

	public static FinalDb getFinalDB(Context context) {
		return FinalDb.create(context, "educational_system", true, 2, new FinalDb.DbUpdateListener() {

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			}
		});
	}

	/**
	 * 保存回复或笔记到数据库tb_note表
	 * 
	 * @param context
	 * @param reply
	 */
	/*public static void saveReply(Context context, Reply reply) {
		FinalDb finalDb = getFinalDB(context);
		finalDb.save(reply);
	}*/

	/**
	 * 查询所有回复或笔记
	 * 
	 * @param context
	 * @param reply
	 */
	/*public static List<Reply> getReplyById(Context context, String id) {
		FinalDb finalDb = getFinalDB(context);
		return finalDb.findAllByWhere(Reply.class, " id = '" + id + "'");
	}*/

	/**
	 * 保存心情
	 * 
	 * @param context
	 * @param feelingMy
	 */
	/*public static void saveFeelingMy(Context context, FeelingMy feelingMy) {
		FinalDb finalDb = getFinalDB(context);
		finalDb.save(feelingMy);
	}*/

	/**
	 * 获取心情
	 * 
	 * @param context
	 */
	/*public static List<FeelingMy> getFeelingMy(Context context) {
		FinalDb finalDb = getFinalDB(context);
		return finalDb.findAll(FeelingMy.class, "time desc");
	}*/

	/**
	 * 删除心情
	 * 
	 * @param context
	 */
	/*public static void delFeelingMy(Context context, FeelingMy feelingMy) {
		FinalDb finalDb = getFinalDB(context);
		finalDb.delete(feelingMy);
	}*/



	/**
	 * 保存我的信息
	 * 
	 * @param context
	 * @param me
	 */
	public static void saveMe(Context context, Me me) {
		FinalDb finalDb = getFinalDB(context);
		// 确保数据库里只有一个用户信息
		finalDb.deleteAll(Me.class);
		finalDb.save(me);
	}

	/**
	 * 获取我的信息
	 * 
	 * @param context
	 */
	public static List<Me> getMe(Context context) {
		FinalDb finalDb = getFinalDB(context);
		return finalDb.findAll(Me.class);
	}


	/**
	 * 检查是否可以登录
	 * 
	 * @param context
	 * @param account
	 * @return
	 */
	public static boolean checkLogin(Context context, String account, String pwd) {
		FinalDb finalDb = getFinalDB(context);
		List<User> userList = finalDb.findAllByWhere(User.class, " account = '" + account + "' and password = '" + pwd
				+ "'");
		return userList != null && userList.size() > 0;
	}

	/**
	 * 检查用户是否注册过账号
	 * 
	 * @param context
	 * @param account
	 * @return
	 */
	public static boolean checkSign(Context context, String account) {
		FinalDb finalDb = getFinalDB(context);
		List<User> userList = finalDb.findAllByWhere(User.class, " account = '" + account + "'");
		return userList != null && userList.size() > 0;
	}

	/**
	 * 保存用户
	 * 
	 * @param context
	 * @param user
	 */
	public static void saveUser(Context context, User user) {
		FinalDb finalDb = getFinalDB(context);
		if (checkSign(context, user.getAccount())) {
			// 保存用户
			finalDb.deleteAll(User.class);
		}
		finalDb.save(user);
	}

	public static User getUserByCount(Context context, String account) {
		FinalDb finalDb = getFinalDB(context);
		List<User> userList = finalDb.findAllByWhere(User.class, " account = '" + account + "'");
		return userList.get(0);
	}

	public static User getUser(Context context) {
		FinalDb finalDb = getFinalDB(context);
		List<User> userList = finalDb.findAll(User.class);
		return userList.get(0);
	}

	/**
	 * 修改密码
	 * 
	 * @param context
	 * @param user
	 */
	public static void changePwdByAccount(Context context, String account, String newPwd) {
		FinalDb finalDb = getFinalDB(context);
		List<User> userList = finalDb.findAllByWhere(User.class, " account = '" + account + "'");
		if (userList != null && userList.size() > 0) {
			// 有这个用户先删除
			delUserByCount(context, account);
			User user = userList.get(0);
			user.setPassword(newPwd);
			// 再保存
			finalDb.save(user);
		}
	}

	/**
	 * 删除用户
	 * 
	 * @param context
	 */
	public static void delUserByCount(Context context, String account) {
		FinalDb finalDb = getFinalDB(context);
		finalDb.deleteByWhere(User.class, " account = '" + account + "'");
	}

	public static void updateLearning(Context context, String id, String key) {
		FinalDb finalDb = getFinalDB(context);
		String sql = "update tb_learning set " + key + " = '1' where id = '" + id + "'";
		SqlInfo sqlInfo = new SqlInfo();
		sqlInfo.setSql(sql);
		sqlInfo.setBindArgs(new LinkedList<Object>());
		finalDb.exeSqlInfo(sqlInfo);
	}


	// /**
	// * 閺屻儴顕楅弻鎰嚋缁鍩嗘稉瀣畱 缁崵绮洪崠鍛邦棅娣囶噣銆� 婵★拷 閹碉拷娴犮儱寮哥�瑰墎绨跨憗鍛畱缁崵绮洪崠锟�
	// *
	// * @param context
	// */
	// public static List<User> getDecorationsByType(Context context, String
	// type) {
	// FinalDb finalDb = getFinalDB(context);
	// return finalDb
	// .findAllByWhere(User.class, " houseType = '" + type + "'");
	// }
	//
	// /**
	// * 娣囨繂鐡ㄩ幍锟介張澶庮棅娣囶噣銆嶇亸蹇曡
	// *
	// * @param context
	// * @param list
	// * 鐟佸懍鎱ㄦい鐟扮毈缁娉﹂崥锟�
	// */
	// public static void saveParents(Context context, List<DecorationsParent>
	// list) {
	// FinalDb finalDb = getFinalDB(context);
	// // finalDb.dropTable(Province.class);
	// // if (list != null && list.size() > 0) {
	// // try {
	// // for (Province item : list) {
	// // finalDb.save(item);
	// // }
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// // }
	// long beginTime = System.currentTimeMillis();
	// SQLiteDatabase db = finalDb.getDb();
	// db.beginTransaction();
	// try {
	// // finalDb.deleteAll(DecorationsChild.class);
	// for (DecorationsParent item : list) {
	// finalDb.lhlSaveLHL(item);
	// }
	// db.setTransactionSuccessful();
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.i("aaa_parent", "瀵倸鐖舵禍锟� 鐎涙ɑ鏆熼幑顔肩氨");
	// } finally {
	// db.endTransaction();
	// long endTime = System.currentTimeMillis();
	// Log.i("aaa_parent", (endTime - beginTime) + "");
	// }
	// }
	//
	// /**
	// * 閼惧嘲褰囬幍锟介張澶庮棅娣囶噣銆嶆径褏琚�
	// *
	// * @param context
	// * @return 鐟佸懍鎱ㄦい鐟般亣缁拷
	// */
	// public static List<DecorationsParent> getParents(Context context) {
	// FinalDb finalDb = getFinalDB(context);
	// return finalDb.findAll(DecorationsParent.class);
	// }
	//
	// /**
	// * 娣囨繂鐡ㄩ幍锟介張澶庮棅娣囶噣銆嶇亸蹇曡
	// *
	// * @param context
	// * @param list
	// * 鐟佸懍鎱ㄦい鐟扮毈缁娉﹂崥锟�
	// */
	// public static void saveChilds(Context context, List<DecorationsChild>
	// list) {
	// FinalDb finalDb = getFinalDB(context);
	// // finalDb.dropTable(Province.class);
	// // if (list != null && list.size() > 0) {
	// // try {
	// // for (Province item : list) {
	// // finalDb.save(item);
	// // }
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// // }
	// long beginTime = System.currentTimeMillis();
	// SQLiteDatabase db = finalDb.getDb();
	// db.beginTransaction();
	// try {
	// // finalDb.deleteAll(DecorationsChild.class);
	// for (DecorationsChild item : list) {
	// finalDb.lhlSaveLHL(item);
	// }
	// db.setTransactionSuccessful();
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.i("aaa_child", "瀵倸鐖舵禍锟� 鐎涙ɑ鏆熼幑顔肩氨");
	// } finally {
	// db.endTransaction();
	// long endTime = System.currentTimeMillis();
	// Log.i("aaa_child", (endTime - beginTime) + "");
	// }
	// }
	//
	// /**
	// * 閼惧嘲褰囬幍锟介張澶庮棅娣囶噣銆嶇亸蹇曡 闁俺绻冩径褏琚惃鍒琩
	// *
	// * @param context
	// * @param parentId
	// * 婢堆呰閻ㄥ埇d
	// * @return 鐟佸懍鎱ㄦい鐟扮毈缁拷
	// */
	// public static List<DecorationsChild> getChildsById(Context context,
	// long parentId) {
	// FinalDb finalDb = getFinalDB(context);
	// return finalDb.findAllByWhere(DecorationsChild.class,
	// " goodsSecondId = '" + parentId + "'");
	// }
	//
	// /**
	// * 娣囨繂鐡ㄩ幍锟介張澶婄
	// *
	// * @param context
	// * @param list
	// * 鐢倿娉﹂崥锟�
	// */
	// /*
	// * public static void saveCityList(Context context, List<City> list) {
	// * FinalDb finalDb = getFinalDB(context); //
	// finalDb.dropTable(City.class);
	// * // if (list != null && list.size() > 0) { // try { // for (City item :
	// * list) { // finalDb.save(item); // } // } catch (Exception e) { //
	// * e.printStackTrace(); // } // } long beginTime =
	// * System.currentTimeMillis(); SQLiteDatabase db = finalDb.getDb();
	// * db.beginTransaction(); try { finalDb.deleteAll(City.class); for (City
	// * item : list) { finalDb.lhlSaveLHL(item); }
	// db.setTransactionSuccessful();
	// * } catch (Exception e) { e.printStackTrace(); } finally {
	// * db.endTransaction(); long endTime = System.currentTimeMillis();
	// * Log.i("aaa", (endTime - beginTime) + ""); } }
	// *//**
	// * 閼惧嘲褰囬幍锟介張澶婄闂嗗棗鎮�
	// *
	// * @param context
	// * @param pid
	// * 閺嶈宓乸rovinceId 閸楀磭娓锋禒绲燿閺夈儴骞忛崣鏍︾瑓鏉堟牕绔�
	// * @return 閹碉拷閺堝娓锋禒浠嬫肠閸氾拷
	// */
	// /*
	// * public static List<City> getCityList(Context context, String pid) {
	// * FinalDb finalDb = getFinalDB(context); Log.i("aaa", pid); return
	// * finalDb.findAllByWhere(City.class, " pid = '" + pid + "'"); }
	// */
	//
	// /**
	// *
	// 妫ｆ牠銆夐幖婊呭偍閿涘苯婀撮崶鐐偝缁鳖澁绱濋柅姘珶閹电偓鍩ч幖婊呭偍閺冨爼鍏樻导姘壌閹诡喖鐓勭敮淇癲閸滃ype鏉╂稖顢戦崢鍡楀蕉鐠佹澘缍嶉惃鍕摠閸岋拷
	// *
	// * @param context
	// * @param item
	// */
	// /*
	// * public static void saveSearchItem(Context context, SearchItem item) {
	// * FinalDb finalDb = getFinalDB(context); List<SearchItem> list =
	// * finalDb.findAllByWhere(SearchItem.class, " keyword = '" +
	// * item.getKeyword() + "' and cityId = ' " + item.getCityId() + "'");
	// *
	// * //闂冨弶顒涢柌宥咁槻娣囨繂鐡� if (list == null || list.size() <= 0) {
	// * finalDb.saveBindId(item); }else{//閸氾箑鍨弴瀛樻煀 finalDb.update(item,
	// * " keyword = '" + item.getKeyword() + "' and cityId = '" +
	// * item.getCityId() + "'"); } }
	// *//**
	// * 閹兼粎鍌ㄩ悾宀勬桨閹兼粎鍌ㄧ拋鏉跨秿閻ㄥ嫬鍤遍弫锟�
	// *
	// * @param cityId
	// * 閸╁骸绔秈d
	// * @author 妤傛鎲�
	// */
	// /*
	// * public static List<SearchItem> getSearchItemByCity(Context context,
	// long
	// * cityId) { FinalDb finalDb = getFinalDB(context); if
	// * (!finalDb.tableIsExist(TableInfo.get(SearchItem.class))) return new
	// * ArrayList<SearchItem>(); return
	// finalDb.findAllByWhere(SearchItem.class,
	// * " cityId=" + cityId, " time DESC ", " limit 0,10 "); }
	// *//**
	// * 閻╊喚娈戦崷鐗堟偝缁便垹鍨归梽銈呭坊閸欒尪顔囪ぐ锟�
	// *
	// * @param context
	// * @param cityId
	// * 閸╁骸绔秈d
	// */
	// /*
	// * public static void deleteSearchesByType(Context context, long cityId,
	// * long type) { FinalDb db = getFinalDB(context); String where =
	// " cityId="
	// * + cityId + " and " + " keywordType=" + type;
	// * db.deleteByWhere(SearchItem.class, where); }
	// *//**
	// * 娴犲簼瀵岄悾宀勬桨閻愮懓绱戦惃鍕偝缁便垽銆夐敍锟� 閸掔娀娅庨崢鍡楀蕉鐠佹澘缍� keywordType 10鐞涖劎銇氶惄顔炬畱閸﹀府绱�
	// 11鐞涖劎銇氶柅姘珶閹电偓鍩�
	// *
	// * @param context
	// * @param cityId
	// * 閸╁骸绔秈d
	// */
	// /*
	// * public static void deleteSearchesExt(Context context, long cityId) {
	// * FinalDb db = getFinalDB(context); String where = " cityId=" + cityId +
	// * " and " + " keywordType<>10 and keywordType<>11 ";
	// * db.deleteByWhere(SearchItem.class, where); }
	// *//****************************************************
	// * 娣囨繂鐡ㄩ崺搴＄ 閻ㄥ嫰娉﹂崥鍫滀繆閹拷
	// *
	// * @param context
	// * @return
	// */
	// /*
	// * public static boolean saveTabCityList(Context context, List<TabCity>
	// * cityList) { FinalDb finalDb = getFinalDB(context);
	// * finalDb.dropTable(TabCity.class); if (cityList != null &&
	// cityList.size()
	// * > 0) { try { for (TabCity item : cityList) {
	// * LogUtils.e("saveTabCityList", item.getName()); finalDb.save(item); } }
	// * catch (Exception e) { e.printStackTrace(); return false; } }
	// *
	// * return true; }
	// *//****************************************************
	// * 娣囨繂鐡ㄩ弬鐗堝复閸欙絽鐓勭敮锟� 閻ㄥ嫰娉﹂崥鍫滀繆閹拷
	// *
	// * @param context
	// * @return
	// */
	// /*
	// * public static boolean saveNewCityList(Context context, List<CityEntity>
	// * cityList) { FinalDb finalDb = getFinalDB(context);
	// * finalDb.dropTable(CityEntity.class); if (cityList != null &&
	// * cityList.size() > 0) { try { for (CityEntity item : cityList) {
	// * LogUtils.e("saveTabCityList", item.getName()); finalDb.save(item); } }
	// * catch (Exception e) { e.printStackTrace(); return false; } }
	// *
	// * return true; }
	// *
	// * public static List<TabCity> getTabCityList(Context context) { FinalDb
	// * finalDb = getFinalDB(context); return finalDb.findAll(TabCity.class); }
	// *
	// * public static List<CityEntity> getNewCityList(Context context) {
	// FinalDb
	// * finalDb = getFinalDB(context); return
	// finalDb.findAll(CityEntity.class);
	// * }
	// *
	// * public static List<CityEntity> getNewCityList(Context context, int
	// mode)
	// * { FinalDb finalDb = getFinalDB(context); return
	// * finalDb.findAllByWhere(CityEntity.class, "mode='" + mode + "'"); }
	// *
	// * public static CityEntity getNewCityEntity(Context context, int cityId)
	// {
	// * FinalDb finalDb = getFinalDB(context); List<CityEntity> list =
	// * finalDb.findAllByWhere(CityEntity.class, "cityId='" + cityId + "'"); if
	// * (list != null && list.size() > 0) { return list.get(0); } return null;
	// }
	// *//****************************************************
	// * 娣囨繂鐡ㄩ崠鍝勭厵 閻ㄥ嫰娉﹂崥鍫滀繆閹拷
	// *
	// * @param context
	// * @return
	// */
	// /*
	// * public static boolean saveTabRegionList(Context context,
	// List<TabRegion>
	// * regionList, long cityId) { FinalDb finalDb = getFinalDB(context); //
	// drop
	// * table before // finalDb.dropTable(TabRegion.class); SQLiteDatabase db =
	// * finalDb.getDb(); db.beginTransaction(); try { if
	// * (getRegionList(context).size() == 0) { finalDb.save(new
	// TabRegion("娑撳秹妾�",
	// * "0", cityId)); if (regionList != null && regionList.size() > 0) { for
	// * (TabRegion item : regionList) { item.setCityId(cityId);
	// * finalDb.save(item); } } db.setTransactionSuccessful(); } } catch
	// * (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
	// }
	// * finally { db.endTransaction(); } return false; }
	// *
	// * public static List<TabRegion> getRegionList(Context context) { FinalDb
	// * finalDb = getFinalDB(context); return
	// * finalDb.findAllByWhere(TabRegion.class, "cityId='" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); }
	// *//**
	// * 娣囨繂鐡ㄩ崯鍡楁箑 閻ㄥ嫰娉﹂崥鍫滀繆閹拷
	// *
	// * @param context
	// * @return
	// */
	// /*
	// * public static boolean saveTabRegionZoneList(Context context,
	// * List<TabRegionZone> regionStationList, long cityId) { FinalDb finalDb =
	// * getFinalDB(context); // drop table before //
	// * finalDb.dropTable(TabRegionZone.class); // finalDb.saveBindId(new
	// * TabRegionZone("娑撳秹妾�", regionStationList.get(0).getFatherIndex(),
	// cityId));
	// * if (getRegionZoneListByWhere(context).size() == 0) { if
	// * (regionStationList != null && regionStationList.size() > 0) { for
	// * (TabRegionZone item : regionStationList) { item.setCityId(cityId);
	// * finalDb.saveBindId(item); } } } return false; }
	// *
	// * public static List<TabRegionZone> getRegionZoneListByWhere(Context
	// * context, String myId) { FinalDb finalDb = getFinalDB(context); if (myId
	// * == null || myId.equals("")) myId = "0"; return
	// * finalDb.findAllByWhere(TabRegionZone.class, " fatherIndex='" + myId +
	// * "' and cityId='" + DDLoginSDK.initDDSDK(context).getCityId() + "'"); //
	// * return finalDb.findAllByWhere(TabRegionZone.class, "fatherIndex="+myId,
	// * "districtCode"); }
	// *
	// * public static List<TabRegionZone> getRegionZoneListByWhere(Context
	// * context) { FinalDb finalDb = getFinalDB(context); return
	// * finalDb.findAllByWhere(TabRegionZone.class, "cityId='" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); // return
	// * finalDb.findAllByWhere(TabRegionZone.class, "fatherIndex="+myId,
	// * "districtCode"); }
	// *//*******************************************************
	// * 娣囨繂鐡ㄩ崷浼存惂缁捐儻鐭� 閻ㄥ嫰娉﹂崥鍫滀繆閹拷
	// *
	// * @param context
	// * @param subList
	// * @return
	// */
	// /*
	// * public static boolean saveSubwayList(Context context, List<TabSubway>
	// * subList, long cityId) { FinalDb finalDb = getFinalDB(context); // drop
	// * table before SQLiteDatabase db = finalDb.getDb();
	// db.beginTransaction();
	// * try { // finalDb.deleteByWhere(TabSubway.class, "cityId='" + cityId +
	// * "'"); if (getSubwayList(context).size() == 0) { finalDb.save(new
	// * TabSubway("娑撳秹妾�", "0", cityId)); if (subList != null && subList.size()
	// >
	// * 0) { for (TabSubway item : subList) { item.setCityId(cityId);
	// * finalDb.save(item); } } } LogUtils.e("saveSubwayList", "success");
	// * db.setTransactionSuccessful(); } catch (Exception e) {
	// * e.printStackTrace(); } finally { db.endTransaction(); } return false; }
	// *//**
	// * 娣囨繂鐡ㄩ崷浼存惂缁旀瑧鍋� 閻ㄥ嫰娉﹂崥鍫滀繆閹拷
	// *
	// * @param context
	// * @return
	// */
	// /*
	// * public static boolean saveSubwayStationList(Context context,
	// * List<TabSubwayStation> subListSta, long cityId) { FinalDb finalDb =
	// * getFinalDB(context); // drop table before //
	// * finalDb.dropTable(TabSubwayStation.class); // finalDb.saveBindId(new
	// * TabSubwayStation("娑撳秹妾�", subListSta.get(0).getFatherIndex(), cityId));
	// if
	// * (getSubwayStationListByWhere(context).size() == 0) if (subListSta !=
	// null
	// * && subListSta.size() > 0) { for (TabSubwayStation item : subListSta) {
	// * item.setCityId(cityId); finalDb.saveBindId(item); } } return false; }
	// *
	// * public static boolean saveSubwayStation(Context context,
	// TabSubwayStation
	// * item) { FinalDb finalDb = getFinalDB(context);
	// finalDb.saveBindId(item);
	// * return false; }
	// *
	// * public static List<TabSubway> getSubwayList(Context context) { FinalDb
	// * finalDb = getFinalDB(context); return
	// * finalDb.findAllByWhere(TabSubway.class, "cityId='" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); }
	// *
	// * public static List<TabSubwayStation>
	// getSubwayStationListByWhere(Context
	// * context, String myId) { FinalDb finalDb = getFinalDB(context); if (myId
	// * == null || myId.equals("")) myId = "0"; return
	// * finalDb.findAllByWhere(TabSubwayStation.class, "fatherIndex='" + myId +
	// * "' and cityId='" + DDLoginSDK.initDDSDK(context).getCityId() + "'"); //
	// * return finalDb.findAllByWhere(TabRegionZone.class, "fatherIndex="+myId,
	// * "districtCode"); }
	// *
	// * public static List<TabSubwayStation>
	// getSubwayStationListByWhere(Context
	// * context) { FinalDb finalDb = getFinalDB(context); return
	// * finalDb.findAllByWhere(TabSubwayStation.class, "cityId='" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); // return
	// * finalDb.findAllByWhere(TabRegionZone.class, "fatherIndex="+myId,
	// * "districtCode"); }
	// *//*********************************************************
	// * 妫板嫮瀹抽惇瀣煣閻╃鍙�
	// *
	// * @param context
	// * @return
	// */
	// /*
	// *
	// * public static boolean saveLookIds(Context context, Province li) {
	// FinalDb
	// * finalDb = getFinalDB(context); List<Province> list =
	// * finalDb.findAllByWhere(Province.class, " lookid = '" + li.getLookid() +
	// * "' and  phone = '" + li.getPhone() + "' and cityId = '" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); if (list != null &&
	// * list.size() <= 0) { finalDb.saveBindId(li); return true; } return
	// false;
	// * }
	// *
	// * // public static void saveAllLookIds(Context context, String userPhone,
	// * List<Integer> priductIds) { // FinalDb finalDb = getFinalDB(context);
	// //
	// * //閻滄澘鐨㈤幍锟介張澶嬫拱閸︽壆娈戠拠銉ф暏閹撮娈戦幋鎸庣爱娣団剝浼呴崚鐘绘珟 閺囨寧宕查幋鎰箛閸斺�虫珤閻拷 // //
	// String where =
	// " phone ='"
	// * + userPhone+"'" ; // // finalDb.deleteByWhere(Province.class, where);
	// //
	// * // for (Integer id : priductIds) { // finalDb.updateBindId(new
	// * Province(userPhone,id+"")); // } // } public static void
	// * saveAllLookIds(Context context, String userPhone, List<Province>
	// lookIds)
	// * { FinalDb finalDb = getFinalDB(context); //
	// 閻滄澘鐨㈤幍锟介張澶嬫拱閸︽壆娈戠拠銉ф暏閹撮娈戦幋鎸庣爱娣団剝浼呴崚鐘绘珟
	// * 閺囨寧宕查幋鎰箛閸斺�虫珤閻拷 String where = " phone ='" + userPhone +
	// "' and cityId = '"
	// +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'";
	// * finalDb.deleteByWhere(Province.class, where);
	// *
	// * for (Province lookId : lookIds) { finalDb.saveBindId(new
	// * Province(userPhone, lookId.getLookid() + "", lookId.getHouseState(),
	// * (long) DDLoginSDK.initDDSDK(context).getCityId())); } }
	// *
	// * public static List<Province> getAllLookIdsByUser(Context context,
	// String
	// * phone) { FinalDb db = getFinalDB(context); return
	// * db.findAllByWhere(Province.class, "phone = '" + phone + "'" +
	// * " and cityId = '" + DDLoginSDK.initDDSDK(context).getCityId() + "'");
	// //
	// * return db.findAllByWhere(Province.class,
	// * "phone = '"+phone+"'"+" and houseState = '1'"); }
	// *
	// * public static int getLookIdsSizeByUser(Context context, String phone) {
	// * try { FinalDb db = getFinalDB(context); // List<Province> list =
	// * db.findAllByWhere(Province.class, "phone = '"+phone+"'");
	// List<Province>
	// * list = db.findAllByWhere(Province.class, "phone = '" + phone + "'" +
	// * " and houseState = '2'" + " and cityId = '" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); if (list != null) {
	// * return list.size(); } } catch (Exception e) { // TODO Auto-generated
	// * catch block e.printStackTrace(); } return 0; }
	// *
	// * public static void deleteLookIdByUser(Context context, String phone,
	// * String itemId) { if (itemId == null || "".equals(itemId.trim())) {
	// * return; } FinalDb finalDb = getFinalDB(context); String where =
	// * " lookid ='" + itemId + "' and  phone = '" + phone + "'" +
	// * " and cityId = '" + DDLoginSDK.initDDSDK(context).getCityId() + "'";
	// * finalDb.deleteByWhere(Province.class, where); }
	// *
	// * public static void deleteSubmitedLookIdsByUser(Context context, String
	// * phone, String ids) { if (phone == null || "".equals(phone.trim()) ||
	// ids
	// * == null || "".equals(ids.trim())) { return; } FinalDb finalDb =
	// * getFinalDB(context); String[] idsArr = ids.split(","); for (String
	// lookid
	// * : idsArr) { String where = " phone ='" + phone + "' and  lookid = '" +
	// * lookid + "'" + " and cityId = '" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'";
	// * finalDb.deleteByWhere(Province.class, where); } }
	// *
	// * public static boolean isLookIdHas(Context context, String phone, String
	// * id) { FinalDb db = getFinalDB(context); List<Province> list =
	// * db.findAllByWhere(Province.class, " lookid='" + id + "' and  phone = '"
	// +
	// * phone + "'" + " and cityId = '" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); if (list != null) {
	// * return list.size() > 0; } return false; }
	// *
	// * public static boolean checkInvailedLookids(Context context, User user,
	// * List<Rent> rentList) { FinalDb db = getFinalDB(context); List<Province>
	// * list = db.findAllByWhere(Province.class, " phone = '" +
	// user.getMobile()
	// * + "'" + " and cityId = '" + DDLoginSDK.initDDSDK(context).getCityId() +
	// * "'"); boolean isHasInvailedLookid = false;// 閺堝妫ゆ稉瀣仸閻ㄥ嫭鍩у┃鎭慸 if (list
	// !=
	// * null) { List<Province> dels = new ArrayList<Province>(); for (Province
	// * item : list) { String id = item.getLookid(); for (Rent rent : rentList)
	// {
	// * if (id.equals(rent.getId() + "")) { dels.add(item); break; } } }
	// * list.removeAll(dels); if (list.size() == 0)// 閹存寧绨崗銊╁劥閸︺劍鐏� return
	// false;
	// for
	// * (Province id : list) { String where = " phone ='" + user.getMobile() +
	// * "' and  lookid = '" + id.getLookid() + "'" + " and cityId = '" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'";
	// * db.deleteByWhere(Province.class, where); isHasInvailedLookid = true; }
	// //
	// * 鐏忓棙妫ら弫鍫礄瀹歌弓绗呴弸璁圭礆 閻ㄥ埇d娴犲孩鏆熼幑顔肩氨闁插苯鍨归梽锟� StringBuffer sb = new
	// StringBuffer(); for
	// * (int i = 0; i < list.size(); i++) { sb.append(list.get(i).getLookid() +
	// * ","); } String str = sb.toString(); Build4SearchHouse build = new
	// * Build4SearchHouse(); DataService.deleteShopping(user.getId(),
	// * str.substring(0, str.length() - 1), build.getMap()); } return
	// * isHasInvailedLookid; }
	// *//*********************************************************
	// * 娣囨繂鐡ㄧ亸蹇撳隘娣団剝浼�
	// *
	// * @param context
	// * @param cityId
	// * @return
	// */
	// /*
	// *
	// * public static boolean savePlotInfoList(Context context, List<PlotInfo>
	// * list, long cityId) { FinalDb finalDb = getFinalDB(context);
	// * SQLiteDatabase db = finalDb.getDb(); db.beginTransaction();
	// *
	// * try { // finalDb.dropTable(PlotInfo.class);
	// * LogUtils.e("savePlotInfoList", "start: " + System.currentTimeMillis() +
	// * ""); finalDb.checkTableExist(PlotInfo.class); for (PlotInfo item :
	// list)
	// * { item.setCityId(cityId); finalDb.lhlSaveLHL(item); }
	// * db.setTransactionSuccessful();
	// * UserUtil.getInstance().setHasKeyWord(context, cityId + "", true);
	// * LogUtils.e("savePlotInfoList", "success: " + System.currentTimeMillis()
	// +
	// * "");
	// *
	// * } catch (Exception e) { LogUtils.e("savePlotInfoList", "error: " +
	// * System.currentTimeMillis() + ""); e.printStackTrace(); return false; }
	// * finally { LogUtils.e("savePlotInfoList", "end: " +
	// * System.currentTimeMillis() + ""); db.endTransaction(); } return true; }
	// *//*********************************************************
	// * 閸掔娀娅庣亸蹇撳隘娣団剝浼�
	// *
	// * @param context
	// * @return
	// */
	// /*
	// *
	// * public static boolean deletePlotInfoList(Context context) { FinalDb
	// * finalDb = getFinalDB(context); synchronized (finalDb) {
	// * finalDb.dropTable(PlotInfo.class); }
	// *
	// * return true; }
	// *//*********************************************************
	// * 娣囨繂鐡╦push娣団剝浼�
	// *
	// * @param context
	// * @return
	// */
	// /*
	// *
	// * public static void saveJpush(Context context, JPush jp) { FinalDb
	// finalDb
	// * = getFinalDB(context); finalDb.saveBindId(jp); }
	// *
	// * public static int getAllDaiBanSizeByUser(Context context, String phone)
	// {
	// * int totalNum = 0; FinalDb db = getFinalDB(context); List<Province>
	// * listLookids = db.findAllByWhere(Province.class, "phone = '" + phone +
	// "'"
	// * + " and houseState = '2'" + " and cityId = '" +
	// * DDLoginSDK.initDDSDK(context).getCityId() + "'"); if (listLookids !=
	// * null) { totalNum = listLookids.size(); } List<JPush> listJPush =
	// * db.findAllByWhere(JPush.class, "phone = '" + phone + "'"); if
	// (listJPush
	// * != null) { totalNum = totalNum + listJPush.size(); } return totalNum; }
	// *
	// * public static int getJpushSizeByUserAndType(Context context, String
	// * phone, String type) { FinalDb db = getFinalDB(context); List<JPush>
	// list
	// * = db.findAllByWhere(JPush.class, "phone = '" + phone +
	// "' and  type = '"
	// * + type + "'"); if (list != null) { return list.size(); } return 0; }
	// *
	// * public static int getJpushSizeAll(Context context, String phone) {
	// * FinalDb db = getFinalDB(context); List<JPush> list =
	// * db.findAllByWhere(JPush.class, "phone = '" + phone + "'"); if (list !=
	// * null) { return list.size(); } return 0; }
	// *
	// * public static void delJpushByUserAndType(Context context, String phone,
	// * String type) { FinalDb db = getFinalDB(context);
	// * db.deleteByWhere(JPush.class, "phone = '" + phone + "' and  type = '" +
	// * type + "'"); }
	// *//*********************************************************
	// * 娣囨繂鐡ㄧ紒褏鐢婚幖婊呭偍
	// *
	// * @param context
	// * @return
	// */
	// /*
	// *
	// * public static boolean saveContinueSearch(Context context,
	// ContinueSearch
	// * item) { FinalDb finalDb = getFinalDB(context); //閸欘亞鏆�娑擄拷閺壜ゎ唶瑜帮拷
	// * finalDb.deleteAll(ContinueSearch.class); return
	// finalDb.saveBindId(item);
	// * }
	// *
	// * public static void deleteContinueSearch(Context context) { FinalDb
	// * finalDb = getFinalDB(context); finalDb.deleteAll(ContinueSearch.class);
	// }
	// *
	// * public static List<ContinueSearch> getCSList(Context context, int
	// cityId)
	// * { FinalDb finalDb = getFinalDB(context); return
	// * finalDb.findAllByWhere(ContinueSearch.class, " cityId=" + cityId); }
	// *
	// * public static void updateCS(Context context, String key, String value)
	// {
	// * FinalDb finalDb = getFinalDB(context); List<ContinueSearch> list =
	// * finalDb.findAll(ContinueSearch.class); if (list == null || list.size()
	// <=
	// * 0) return; TableInfo table = TableInfo.get(ContinueSearch.class);
	// * StringBuilder sb = new StringBuilder(" update " + table.getTableName()
	// +
	// * " set " + key + " = '" + value + "'"); SqlInfo sqlInfo = new SqlInfo();
	// * sqlInfo.setSql(sb.toString()); sqlInfo.setBindArgs(new
	// * LinkedList<Object>()); finalDb.exeSqlInfo(sqlInfo);
	// *
	// * }
	// *//*********************************************************
	// * 娣囨繂鐡ㄥ☉鍫熶紖
	// *
	// * @param context
	// * @return
	// */
	// /*
	// * public static boolean saveNotification(Context context, Notification
	// * noti) { FinalDb finalDb = getFinalDB(context);
	// finalDb.saveBindId(noti);
	// * return false; }
	// *
	// * public static boolean deleteNotification(Context context,
	// * List<Notification> notis) { if (notis == null || notis.size() == 0) {
	// * return false; } boolean result = true; try { FinalDb finalDb =
	// * getFinalDB(context); StringBuilder where = new
	// * StringBuilder(" _id in ("); for (Notification noti : notis) {
	// * where.append(noti.get_id()).append(","); }
	// * where.deleteCharAt(where.length() - 1); where.append(") ");
	// *
	// * finalDb.deleteByWhere(Notification.class, where.toString()); } catch
	// * (Exception e) { result = false; }
	// *
	// * return result; }
	// *//**
	// * 娣囨繂鐡ㄩ幍锟介張澶嬬垼缁涙儳鍨悰锟�
	// *
	// * @param context
	// * @param tagInfoList
	// */
	// /*
	// * public static void saveAllTagList(Context context, List<TagInfo>
	// * tagInfoList) { FinalDb finalDb = getFinalDB(context); SQLiteDatabase db
	// =
	// * finalDb.getDb(); db.beginTransaction(); //
	// 閻滄澘鐨㈤幍锟介張澶嬫拱閸︽壆娈戠拠銉ф暏閹撮娈戦幋鎸庣爱娣団剝浼呴崚鐘绘珟
	// * 閺囨寧宕查幋鎰箛閸斺�虫珤閻拷 try { finalDb.deleteAll(TagInfo.class); for (TagInfo
	// tagInfo
	// * : tagInfoList) { finalDb.lhlSaveLHL(tagInfo); }
	// * db.setTransactionSuccessful();
	// *
	// * SharedPreferenceManager.getInstance(context).setHasTagsTime(System.
	// * currentTimeMillis());
	// *
	// * } catch (Exception e) { e.printStackTrace(); } finally {
	// * db.endTransaction(); } }
	// *
	// *
	// * public static String getTagByIdandType(Context context, long id, int
	// * type) { try { FinalDb db = getFinalDB(context); // List<Province> list
	// =
	// * db.findAllByWhere(Province.class, "phone = '"+phone+"'"); List<TagInfo>
	// * list = db.findAllByWhere(TagInfo.class, "id = '" + id + "'" +
	// * " and type = '" + type + "'"); if (list != null && list.size() > 0) {
	// * return list.get(0).getName(); } } catch (Exception e) { // TODO
	// * Auto-generated catch block e.printStackTrace(); } return ""; }
	// *
	// * public static List<TagInfo> getTagListByType(Context context, int type)
	// {
	// * List<TagInfo> list = new ArrayList<>(); try { FinalDb db =
	// * getFinalDB(context); // List<Province> list =
	// * db.findAllByWhere(Province.class, "phone = '"+phone+"'"); list =
	// * db.findAllByWhere(TagInfo.class, " type = '" + type + "'"); if (list !=
	// * null && list.size() > 0) { return list; } } catch (Exception e) { //
	// TODO
	// * Auto-generated catch block e.printStackTrace(); } return list; }
	// *
	// *
	// * public static List<String> getTagListSortByTypeAndList(Context context,
	// * int type, List<Integer> list) { List<String> sList = new ArrayList<>();
	// * try { FinalDb db = getFinalDB(context); // List<Province> list =
	// * db.findAllByWhere(Province.class, "phone = '"+phone+"'"); List<TagInfo>
	// * mList = db.findAllByWhere(TagInfo.class, " type = '" + type + "'" +
	// * " order by weight"); if (mList != null && mList.size() > 0) { for
	// * (TagInfo tagInfo : mList) { if
	// * (list.contains(Integer.parseInt(tagInfo.getId() + ""))) {
	// * sList.add(tagInfo.getName()); } } return sList; } } catch (Exception e)
	// {
	// * // TODO Auto-generated catch block e.printStackTrace(); } return sList;
	// }
	// *//**
	// * 娣囨繂鐡ㄩ幍锟介張澶庣槑娴犻攱鐖ｇ粵鎯у灙鐞涳拷
	// *
	// * @param context
	// */
	// /*
	// * public static void saveAllEvaluateList(Context context,
	// * List<EvaluateType> list) { FinalDb finalDb = getFinalDB(context);
	// * SQLiteDatabase db = finalDb.getDb(); db.beginTransaction(); //
	// * 閻滄澘鐨㈤幍锟介張澶嬫拱閸︽壆娈戠拠銉ф暏閹撮娈戦幋鎸庣爱娣団剝浼呴崚鐘绘珟 閺囨寧宕查幋鎰箛閸斺�虫珤閻拷 try {
	// * finalDb.deleteAll(EvaluateType.class); for (EvaluateType evaluateType :
	// * list) { finalDb.lhlSaveLHL(evaluateType); } // if
	// * (map.containsKey("complete")) { // Map<String, Object> completeMap =
	// * (Map<String, Object>) map.get("complete"); // saveMap(completeMap, 1,
	// * finalDb); // } // if (map.containsKey("cancel")) { // Map<String,
	// Object>
	// * cancelMap = (Map<String, Object>) map.get("cancel"); //
	// * saveMap(cancelMap, 1, finalDb); // } db.setTransactionSuccessful();
	// *
	// *
	// * } catch (Exception e) { e.printStackTrace(); } finally {
	// * db.endTransaction(); } }
	// *
	// * private static void saveMap(Map<String, Object> map, int type, FinalDb
	// * finalDb) { for (String key : map.keySet()) { List<EvaluateType>
	// * evaluateTypeList = JSON.parseArray(map.get(key).toString(),
	// * EvaluateType.class); if (evaluateTypeList != null) for (EvaluateType
	// * evaluateType : evaluateTypeList) {
	// * evaluateType.setScore(Integer.parseInt(key));
	// * finalDb.lhlSaveLHL(evaluateType); } } }
	// *
	// * public static List<EvaluateType> getEvaluateTypeListByStar(Context
	// * context, int score, int state) { List<EvaluateType> list = new
	// * ArrayList<>(); try { FinalDb db = getFinalDB(context); list =
	// * db.findAllByWhere(EvaluateType.class, " score = '" + score + "' and " +
	// * "userOrderStatus = '" + state + "'"); if (list != null && list.size() >
	// * 0) { return list; } } catch (Exception e) { // TODO Auto-generated
	// catch
	// * block e.printStackTrace(); } return null; }
	// *
	// * public static String getEvaluateContentById(Context context, int id) {
	// * try { FinalDb db = getFinalDB(context); List<EvaluateType> list =
	// * db.findAllByWhere(EvaluateType.class, " id = '" + id + "'"); if (list
	// !=
	// * null && list.size() > 0) { return list.get(0).getContent(); } } catch
	// * (Exception e) { // TODO Auto-generated catch block e.printStackTrace();
	// }
	// * return ""; }
	// */

}
