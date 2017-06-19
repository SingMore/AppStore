package com.example.appstore.bean;

import java.util.List;

/**
 * Created by SingMore on 2017/3/21.
 */

public class ResponseBean {
    private int code;
    public String text;
    private String url;
    private List<ListItem> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ListItem> getList() {
        return list;
    }

    public void setList(List<ListItem> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ResponseBean [code=" + code + ", text=" + text +
                ", url=" + url +  ", list=" + list + "]";
    }

    class ListItem {
        private String article;
        private String source;
        private String icon;
        private String detailurl;

        private String name;
        private String info;

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return "ListItem [article=" + article + ", source=" + source +
                    ", icon=" + icon +  ", detailurl=" + detailurl +  ", name=" + name +
                    ", info=" + info + "]";
        }
    }
}
