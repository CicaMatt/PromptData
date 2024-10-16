import pandas as pd
from sklearn.preprocessing import LabelEncoder

# Create the DataFrame
df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'], 
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'], 
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 'New_York']
})

# Initialize the LabelEncoder
le = LabelEncoder()

# Apply LabelEncoder to each column
df_encoded = df.apply(lambda col: le.fit_transform(col) if col.dtype == 'object' else col)

# View the encoded DataFrame
print(df_encoded)
