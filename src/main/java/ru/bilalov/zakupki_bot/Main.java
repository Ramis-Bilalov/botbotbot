package ru.bilalov.zakupki_bot;

import com.vdurmont.emoji.EmojiParser;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private String number;
    private String noticeInfoId;

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Tender> htmlParse() throws IOException {
        noticeInfoId = null;

        List<Tender> tenderList = new ArrayList<>();
        String numb = EmojiParser.parseToUnicode(":black_large_square:");
        String dateZ = EmojiParser.parseToUnicode(":watch:");
        String organization = EmojiParser.parseToUnicode(":detective:");
        String name = EmojiParser.parseToUnicode(":memo:");
        String money = EmojiParser.parseToUnicode(":abacus:"); // watch

        String url = "https://zakupki.gov.ru/epz/order/extendedsearch/results.html?searchString=" + number + "&morphology=on&search-filter=%D0%94%D0%B0%D1%82%D0%B5+%D1%80%D0%B0%D0%B7%D0%BC%D0%B5%D1%89%D0%B5%D0%BD%D0%B8%D1%8F&pageNumber=1&sortDirection=false&recordsPerPage=_5&showLotsInfoHidden=false&sortBy=UPDATE_DATE&fz44=on&fz223=on&af=on&ca=on&pc=on&pa=on&currencyIdGeneral=-1";

        Document doc = Jsoup.connect(url).get();

        Elements divElements = doc.getElementsByAttributeValue("class", "row no-gutters registry-entry__form mr-0");

        Elements hrefElements = doc.getElementsByAttributeValue("class", "registry-entry__header-mid__number");

        divElements.forEach(divElement ->

        {
            Element aElement = divElement.child(0);

            String tenderType = aElement.child(0).child(0).text();
            String num = "";
            try {
                num = numb + " № " + aElement.child(0).child(1).child(0).child(0).child(0).text() + " (" + tenderType + ")";
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }


            String sum = "";
            try {
                Element bElement = divElement.child(1);
                sum = money + " " + bElement.child(0).child(1).text();
            } catch (IndexOutOfBoundsException e) {
                sum = money + " Цены нет";
                e.printStackTrace();
            }

            String title = name + " " + aElement.child(1).child(0).child(1).text();


            String date = "Заявки не подаются!!!";
            try {
                if (!tenderType.contains("223-ФЗ Закупка у единственного поставщика (подрядчика, исполнителя)")) {
                    Element dElement = divElement.child(1);
                    date = dateZ + " Подача заявок до: " + dElement.child(1).child(2).text();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

            Element cElement = divElement.child(0);
            String company = organization + " " + cElement.child(1).child(1).child(1).text();

            Element element = hrefElements.first().child(0);
            String url0 = element.attr("href");
            String[] split = url0.split("=");
            noticeInfoId = split[1];
            String urlDoc = "https://zakupki.gov.ru/epz/order/notice/notice223/common-info.html?noticeInfoId=" + noticeInfoId;
            tenderList.add(new Tender(num, sum, title, company, date, urlDoc));
        });
        return tenderList;
    }
}
class Tender {

    private String num;         // номер закупки
    private String title;       // название закупки
    private String company;     // организация
    private String sum;         // стоимость
    private String date;        // дата окончания подачи заявок

    private String url;

    public Tender(String num, String sum, String title, String company, String date, String url) {
        this.num = num;
        this.sum = sum;
        this.title = title;
        this.company = company;
        this.date = date;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return num + "\n" + sum + "\n" + title + "\n" + company + "\n" + date + "\n" + url;
    }
}