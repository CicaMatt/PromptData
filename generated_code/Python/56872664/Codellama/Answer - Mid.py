from sklearn.model_selection import StratifiedShuffleSplit
import numpy as np

# Load your dataset
data = pd.read_csv("your_dataset.csv")

# Create a stratified splitter object
sss = StratifiedShuffleSplit(n_splits=1, test_size=0.2, random_state=42)

# Split the data into training and validation sets
for train_index, val_index in sss.split(data, data["category"]):
    X_train, y_train = data.loc[train_index], data["category"].iloc[train_index]
    X_val, y_val = data.loc[val_index], data["category"].iloc[val_index]

# Split the training set into training and test sets
sss = StratifiedShuffleSplit(n_splits=1, test_size=0.2, random_state=42)
for train_index, val_index in sss.split(X_train, y_train):
    X_train_val, y_train_val = X_train.loc[train_index], y_train.iloc[train_index]
    X_test, y_test = X_train.loc[val_index], y_train.iloc[val_index]

# Ensure that the training, validation, and test sets have the same proportions of categories
X_train_val["category"].value_counts() / len(X_train_val)
X_test["category"].value_counts() / len(X_test)