Тестовое задание Junior Kotlin Developer
Необходимо реализовать игру содержащий следующие экраны (Views) с
соответствующей им логикой:
1. Menu View - главный навигационный экран, содержит кнопки перехода на
другие экраны.
2. Game Scene - экран игры
3. End Game Popup - экран выйгрыша.
В процессе реализации экранов следует быть внимательным к деталям UI/UX
Описание игрового процесса
Игра - найти пары. На Game Scene отображается сетка из карточек, где игроку
необходимо нажимать на конкретные элементы, чтобы найти пары.
Таймер отображает текущее время игры, сколько прошло с момента старта, чем
меньше времени после всех отгаданных карточек, тем больше награда.
Технические требования
1. В качестве игровой валюты в игре выступают Монетки
2. Механика начисления награды в зависимости от времени: если игрок прошел
игру меньше чем за 20 сек, то он получает максимальную награду в 100
монеток.За каждую последующую секунду таймера сверх 20 от максимальной
награды отнимается 5 монеток, при этом минимальная награда, которую игрок
получитвне зависимости от прохождения - 10 монеток.
3. Графика элементов не принципиальна, и можно использовать любые картинки.
4. UI должен корректно реагировать при изменении размеров экрана, не стоит
допускать наложение элементов друг на друга.
Результат тестового задания - стоит записать видео демонстрацию работы игрового
процесса и всех экранов.Игру стоит показать с несколькими финальными результатами
и отобразить получение награды.