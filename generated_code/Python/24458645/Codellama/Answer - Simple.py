import pandas as pd
from sklearn import preprocessing

df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'], 
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'], 
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 
                 'New_York']
})

le = preprocessing.LabelEncoder()
encoded_df = le.transform(df)
print(encoded_df)