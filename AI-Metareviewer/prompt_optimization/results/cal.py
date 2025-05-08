from sklearn.metrics import f1_score

# a = [
#     [0.75, 0.6785714285714286, 0.53125, 0.7931034482758621],
#     [0.6507936507936508, 0.6711409395973155, 0.7441860465116279, 0.6],
#     [0.6451612903225806, 0.6666666666666666, 0.6428571428571429, 0.5517241379310345],
#     [0.6705882352941176, 0.7735849056603774, 0.5555555555555556, 0.6666666666666666]
# ]

# b = [0.0] * 4  # initialize sums
# c = []  # initialize averages

# for i in range(4):
#     for j in range(4):
#         b[i] += a[j][i]
#     c.extend([b[i] / 4])
# print(c)


true_labels = [0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1 , 1, 1,1,1,0,0,0,0,1]
pred_labels = [1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0 ,0, 0,0,0,0,0,0,0,0]
print(f"len(true_labels): {len(true_labels)}")
print(f"len(pred_labels): {len(pred_labels)}")     
f1 = f1_score(true_labels, pred_labels, average='micro')
print(f"{f1}")