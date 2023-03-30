package com.sadanand.civiladvocacyapp.civiladvocacyapp;

import android.os.Parcel;
import android.os.Parcelable;

public class GovMinister implements Parcelable {
    String personal_email,Addr_Details_plain,personal_phone,personal_Website;
    String minister_Post,organization_party,officer_Name;
    String photo,User_Address_line,picture;
    String website_name;

    String facebookLink,twitterLink,youtubeLink;



    public void setOrganization_party(String organization_party) {
        this.organization_party = organization_party;
    }
    public String getMinister_Post() {
        return minister_Post;
    }

    public void setMinister_Post(String minister_Post) {
        this.minister_Post = minister_Post;
    }

    public String getOrganization_party() {
        return organization_party;
    }


    public String getAddr_Details_plain() {
        return Addr_Details_plain;
    }

    public void setAddr_Details_plain(String addr_Details_plain) {
        this.Addr_Details_plain = addr_Details_plain;
    }
    public String getOfficer_Name() {
        return officer_Name;
    }

    public void setOfficer_Name(String officer_Name) {
        this.officer_Name = officer_Name;
    }

    public void setPersonal_email(String personal_email) {
        this.personal_email = personal_email;
    }

    public String getPersonal_phone() {
        return personal_phone;
    }

    public void setPersonal_phone(String personal_phone) {
        this.personal_phone = personal_phone;
    }
    public String getPersonal_email() {
        return personal_email;
    }


    public String getPersonal_Website() {
        return personal_Website;
    }



    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public String getTwitterLink() {
        return twitterLink;
    }
    public void setPersonal_Website(String personal_Website) {
        this.personal_Website = personal_Website;
    }

    public String getFacebookLink() {
        return facebookLink;
    }



    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }
    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPicture() {
        return picture;
    }
    public String getUrl() {
        return website_name;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public void setUrl(String urls) {
        this.website_name = urls;
    }


    public GovMinister() {
    }

    public String getUser_Address_line() {
        return User_Address_line;
    }

    public void setUser_Address_line(String user_Address_line) {
        this.User_Address_line = user_Address_line;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelMethod(dest);
    }

    private void writeToParcelMethod(Parcel dest) {
        dest.writeString(this.twitterLink);
        dest.writeString(this.youtubeLink);
        dest.writeString(this.minister_Post);
        dest.writeString(this.Addr_Details_plain);
        dest.writeString(this.organization_party);
        dest.writeString(this.officer_Name);
        dest.writeString(this.personal_email);
        dest.writeString(this.personal_phone);
        dest.writeString(this.personal_Website);
        dest.writeString(this.photo);
        dest.writeString(this.User_Address_line);
        dest.writeString(this.facebookLink);
    }

    protected GovMinister(Parcel in) {
        boolean flag=false;
        GovMinisterMethod(in,flag);

    }

    private void GovMinisterMethod(Parcel in, boolean flag) {
        this.twitterLink = in.readString();
        this.youtubeLink = in.readString();
        this.minister_Post = in.readString();
        this.Addr_Details_plain = in.readString();
        this.organization_party = in.readString();
        this.officer_Name = in.readString();
        this.personal_email = in.readString();
        this.personal_phone = in.readString();
        this.personal_Website = in.readString();
        this.photo = in.readString();
        this.User_Address_line = in.readString();
        this.facebookLink = in.readString();
        flag=true;
    }

    public static final Creator<GovMinister> CREATOR = new Creator<GovMinister>() {
        @Override
        public GovMinister createFromParcel(Parcel source) {
            return new GovMinister(source);
        }

        @Override
        public GovMinister[] newArray(int size) {
            return new GovMinister[size];
        }
    };
}
