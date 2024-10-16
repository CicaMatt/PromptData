import pandas as pd
from sklearn.preprocessing import LabelEncoder

# Sample DataFrame
df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'], 
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'], 
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 'New_York']
})

# Initialize LabelEncoder
le = LabelEncoder()

# Apply LabelEncoder to each column
for column in df.columns:
    df[column] = le.fit_transform(df[column])

print(df)
