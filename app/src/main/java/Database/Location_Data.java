package Database;

import android.graphics.Bitmap;

public class Location_Data {




	public void setLag(String lag) {
		this.lag = lag;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void setTdate(String tdate) {
		this.tdate = tdate;
	}







	private String lag;
	private String lng;
	private String area;
	private String tdate;
	private String uniqId;







	public String getUniqId() {
		return uniqId;
	}

	public void setUniqId(String uniqId) {
		this.uniqId = uniqId;
	}




	public Location_Data(String lt, String lg, String a, String t, String u) {


		lag = lt;
		lng = lg;
		area= a;
		tdate =t;
		uniqId=u;

	}

	public Location_Data()
	{

	}



	public String getLag() {
		return lag;
	}


	public String getArea() {
		return area;
	}

	public String getLng() {
		return lng;
	}

	public String getTdate() {
		return tdate;
	}

}
