package com.example.appstore.bean;

import java.util.Iterator;
import java.util.List;

/**
 * Created by SingMore on 2017/6/15.
 */

public class DictationResult {
    private int bg;
    private int ed;
    private boolean ls;
    private int sn;
    private List<Word> ws;

    public int getBg()
    {
        return this.bg;
    }

    public int getEd()
    {
        return this.ed;
    }

    public int getSn()
    {
        return this.sn;
    }

    public List<Word> getWs()
    {
        return this.ws;
    }

    public boolean isLs()
    {
        return this.ls;
    }

    public void setBg(int paramInt)
    {
        this.bg = paramInt;
    }

    public void setEd(int paramInt)
    {
        this.ed = paramInt;
    }

    public void setLs(boolean paramBoolean)
    {
        this.ls = paramBoolean;
    }

    public void setSn(int paramInt)
    {
        this.sn = paramInt;
    }

    public void setWs(List<Word> paramList)
    {
        this.ws = paramList;
    }

    public String toString()
    {
        String str = "";
        Iterator localIterator = this.ws.iterator();
        while (true)
        {
            if (!localIterator.hasNext())
                return str;
            Word localWord = (Word)localIterator.next();
            str = str + localWord.toString();
        }
    }

    class Cw
    {
        private int sc;
        private String w;

        Cw()
        {
        }

        public int getSc()
        {
            return this.sc;
        }

        public String getW()
        {
            return this.w;
        }

        public void setSc(int paramInt)
        {
            this.sc = paramInt;
        }

        public void setW(String paramString)
        {
            this.w = paramString;
        }

        public String toString()
        {
            return this.w;
        }
    }

    class Word
    {
        private int bg;
        private List<DictationResult.Cw> cw;

        Word()
        {
        }

        public int getBg()
        {
            return this.bg;
        }

        public List<DictationResult.Cw> getCw()
        {
            return this.cw;
        }

        public void setBg(int paramInt)
        {
            this.bg = paramInt;
        }

        public void setCw(List<DictationResult.Cw> paramList)
        {
            this.cw = paramList;
        }

        public String toString()
        {
            String str = "";
            Iterator localIterator = this.cw.iterator();
            while (true)
            {
                if (!localIterator.hasNext())
                    return str;
                DictationResult.Cw localCw = (DictationResult.Cw)localIterator.next();
                str = str + localCw.toString();
            }
        }
    }
}
