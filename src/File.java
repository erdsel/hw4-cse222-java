public class File extends FileSystemElement {

    public File(String name, FileSystemElement parent) {
        super(name, parent); // Süper sınıfın (FileSystemElement) yapıcı metodunu çağırır
    }

    @Override
    public void print(String prefix) {
        // Bu metod, dosya bilgisini önek ile birlikte yazdırır
        System.out.println(prefix + "File: " + getName());
    }

    // Eğer File sınıfına özgü başka metodlar veya özellikler varsa, burada ekleyebilirsiniz
    // Örneğin, dosya içeriğini okuma veya yazma metodları
}
