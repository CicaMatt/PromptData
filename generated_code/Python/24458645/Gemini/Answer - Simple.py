import pandas as pd
from sklearn import preprocessing

# ... (Your DataFrame creation code remains the same)

# Stack the DataFrame
stacked_df = df.stack()

# Initialize the LabelEncoder
le = preprocessing.LabelEncoder()

# Fit and Transform
encoded_stacked_df = le.fit_transform(stacked_df)

# Reshape back to the original DataFrame structure
encoded_df = pd.Series(encoded_stacked_df, index=stacked_df.index).unstack()

print(encoded_df)