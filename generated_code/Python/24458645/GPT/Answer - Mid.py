import pandas as pd
from sklearn.preprocessing import LabelEncoder

# Create dummy data
df = pd.DataFrame({
    'pets': ['cat', 'dog', 'cat', 'monkey', 'dog', 'dog'], 
    'owner': ['Champ', 'Ron', 'Brick', 'Champ', 'Veronica', 'Ron'], 
    'location': ['San_Diego', 'New_York', 'New_York', 'San_Diego', 'San_Diego', 'New_York']
})

# Initialize a dictionary of LabelEncoders
encoders = {}

# Apply LabelEncoder to each column in the DataFrame
for col in df.columns:
    le = LabelEncoder()
    df[col] = le.fit_transform(df[col])
    encoders[col] = le  # Store the encoder for each column in case you want to inverse_transform later

# Display the transformed DataFrame
print(df)

# Example of how to inverse transform the data back to original labels
for col in df.columns:
    df[col] = encoders[col].inverse_transform(df[col])

# Display the DataFrame after inverse transforming
print(df)
