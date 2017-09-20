# squidLogParserElasticsearch

Default squid access logunu parcalara ayirip elasticsearch'e yazma isini yapmaktadir. Bu projenin amaci java ile elasticsearchun kullanimi anlatilmaya calisilmistir. access.log dosyasi anlik olarak okunmaktadir. bunu linux tarafinda bir daemon yada windows tarafinda bir servis haline getirilebilir. Bu projede loglar big data veritabanina yazilmistir ancak bu veriyi anlamli hale getirmek ve analiz islemleri icin [burada ki](https://github.com/emrahxozel/squidAccessLogMonitor) projeden faydalanilabilir. Projede elasticsearch 5.5.0 versionu ve geo-ip olarak maxmind.geoip 1.2.10(destination ip adreslerinin lokasyonlarini bulmak icin kullanildi) versiyonu kullanilmistir.

#### configuration.properties
Program calistirildiginda config.properties dosyasi yoksa otomatik olusturulur. Eger varsa icerisinde ki bilgiler okunarak ona gore islem yapilir.
lastLine : dosyayi okumaya hangi satirdan baslayacagini belirler 0 ise dosyayi en basindan itibaren okur. Bu deger servis calistigi surece surekli guncellenmektedir.
elkHost: Elasticsearch'un calistigi makinenin ip adresidir.
elkPort: Elasticsearch'un calistigi port.
indexName: Logu yazarken kullanilacak index adi. (program calistiginda index otomatik olusturulur)
typeName: Logu yazarken kullanilacak tip adidir. (program calistiginda type otomatik olusturulur)
logFile: Squid access log dosyasinin tam yoludur.
