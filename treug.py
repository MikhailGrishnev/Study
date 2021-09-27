chisla = []
n =
for i in range(1, 51):
    chisla.append()
chisla.reverse()
sight1, sight2, sight3 = 0, 0, 0


def ner(sight1, sight2, sight3):
    if (sight1 + sight2 > sight3) and (sight1 + sight3 > sight2) and (sight2 + sight3 > sight1):
        p = (sight1 + sight2 + sight3) / 2
        return (p * (p - sight1) * (p - sight2) * (p - sight3)) ** (1 / 2)
    return 0


for i in range(len(chisla)):
    sight1 = chisla[i]
    sight2 = chisla[i + 1]
    sight3 = chisla[i + 2]
    if ner(sight1, sight2, sight3) != 0:
        print(ner(sight1, sight2, sight3))
        print(sight1, sight2, sight3)
        break