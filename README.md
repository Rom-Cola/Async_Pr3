Результати до завдання №1

Work Dealing:
Простий підхід, розподіл задач відбувається на старті.
Плюс: Виконується швидше, якщо підзадачі мають однакове навантаження.

Work Stealing:
Потоки самостійно "крадуть" задачі.
Плюс: Краще справляється з нерівномірним навантаженням.


Висновки
Якщо масив невеликий і добре розділений, Work Dealing працюватиме швидше.
У великих масивах із нерівномірним розподілом елементів перевагу буде за Work Stealing.

У контексті моєї роботи важко сказати, що з цього буде працювати швидше.


Результати до завдання №2

У цьому проєкті я використав Work Stealing, оскільки він ефективно розподіляє навантаження між потоками, 
що особливо важливо для задачі пошуку зображень у великій директорії з численними підкаталогами. 

Ось чому цей підхід був оптимальним:

У нашій задачі кожен підкаталог може містити різну кількість файлів. Замість того, щоб кожен потік обробляв фіксовану частину даних, 
Work Stealing дозволяє потокам "красти" підзадачі у інших, що дозволяє ефективно використовувати ресурси і зменшити час на обробку, якщо деякі каталоги містять значно більше файлів.
