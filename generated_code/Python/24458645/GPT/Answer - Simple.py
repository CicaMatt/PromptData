import pandas as pd
from sklearn import preprocessing

# Sample DataFrame
df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'], 
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'], 
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 
                 'New_York']
})

# Create a function to apply LabelEncoder to each column
def label_encode(df):
    le = preprocessing.LabelEncoder()
    return df.apply(lambda col: le.fit_transform(col))

# Apply the function to the entire DataFrame
encoded_df = label_encode(df)

print(encoded_df)
