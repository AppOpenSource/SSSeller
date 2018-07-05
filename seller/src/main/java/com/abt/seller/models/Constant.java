package com.abt.seller.models;

public class Constant {
	private static final String webHost = "http://121.199.13.117/beta/index.php";
//	public static final String loginUri = webHost + "/PhoneShop/Login";
	public static final String loginUri = webHost + "/Apishops/login";
//	public static final String uploadImgURL = webHost + "/PhoneShop/Upload";
	public static final String uploadImgURL = webHost + "/Apishops/upload";
//	public static final String curMsgUri = webHost + "/PhoneMessage/ShopUserMsg";
	public static final String curMsgUri = webHost + "/Apishops/shopsinfor";
	public static final String msgCountUri = webHost + "/PhoneMessage/ShopMsgNum";
	public static final String BefMsgUri = webHost + "/PhoneMessage/ShopMsgAfter";
	public static final String AftMsgUri = webHost + "/PhoneMessage/ShopMsgBefore";
	public static final String orderUri = webHost + "/PhoneShop/IndexOrder";
//	public static final String orderListUri = webHost + "/PhoneShop/OrderList";
	public static final String orderListUri = webHost + "/Apishops/orderlist";
//	public static final String orderDetailUri = webHost	+ "/PhoneShop/OrderDetail";
	public static final String orderDetailUri = webHost	+ "/Apishops/ordervo";
	public static final String orderTrackUri = webHost	+ "/Apishops/ordertrack";
//	public static final String orderConfirmUri = webHost + "/PhoneShop/OrderConfirm";
	public static final String orderUploadUri = webHost	+ "/Apishops/uporder";
//	public static final String goodsListUri = webHost + "/PhoneShop/GoodsList";
	public static final String goodsListUri = webHost + "/Apishops/goodslist";
	public static final String goodsTypeUri = webHost + "/Apishops/goodstype";
	public static final String goodsTypeAddUri = webHost + "/Apishops/addtype";
	public static final String goodsDetailUri = webHost + "/Apishops/goodsvo";
	public static final String goodsOperateUri = webHost + "/PhoneShop/GoodsOperate";
	public static final String goodsDelUri = webHost + "/PhoneShop/GoodsDel";
//	public static final String goodsSaveUri = webHost + "/PhoneShop/GoodsSave";
	public static final String goodsSaveUri = webHost + "/Apishops/dogoods";
	public static final String UPDATE_SERVER = webHost + "/Update/AndriodUpdate";
	public static final String UPDATE_SAVENAME = "SSSeller.apk";

	public static final int notForSale = 0;
	public static final int readTimeOut = 10 * 1000;
	public static final int connectTimeout = 10 * 1000;
	public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
	public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
	public static final int UPLOAD_SUCCESS_CODE = 1;
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	public static final int WHAT_TO_UPLOAD = 1;
	public static final int WHAT_UPLOAD_DONE = 2;
	public static final String KEY_PHOTO_PATH = "photo_path";
	public static final String SELLERID = "2";
}