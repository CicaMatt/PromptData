import pandas as pd
from sklearn import preprocessing

df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'], 
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'], 
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 
                 'New_York']
})

# One-hot encode all columns at once using pd.get_dummies
df = pd.get_dummies(df, drop_first=True)

# Use LabelEncoder on the one-hot encoded dataframe
le = preprocessing.LabelEncoder()
le.fit(df)