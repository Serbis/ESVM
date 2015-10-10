package esvm.vm.desc;

/**
 * Класс описания адреса указателя. Содержит номер страницы и смещение по ей.
 * ЗАПОМНИ, ОБРАЩЕНИЕ ПО АДРЕСУ ИДЕТ НЕ ОТ 0 А ОТ ОДНОГО, ПОТОМУ ЧТО НЕЛЬЗЯ
 * МНОЖИТЬ НА НОЛЬ
 */
public class Pointer {
    public int page;
    public int offset;

    public Pointer(int page, int offset) {
        this.page = page;
        this.offset = offset;
    }
}
